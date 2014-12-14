package matze.gps.icu.monitor;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.control.PhoneNumberManager;
import matze.gps.icu.model.ICUSMS;
import matze.gps.icu.model.Requests;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	// private static final String ARG_SECTION_NUMBER = "section_number";
	TextView textViewNumber;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static MainFragment newInstance(int sectionNumber) {
		MainFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		// args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == Requests.PICK_CONTACT_REQUEST) {
			// Make sure the request was successful
			if (resultCode == -1) {

				Log.i("debug", "resultCode " + resultCode);
				Log.i("debug", "resultCode " + data.getPackage());

				Uri contactUri = data.getData();

				String[] projection = { Phone.NUMBER };

				Cursor cursor = getActivity().getContentResolver().query(contactUri, projection, null, null, null);
				cursor.moveToFirst();

				int column = cursor.getColumnIndex(Phone.NUMBER);
				String number = cursor.getString(column);

				PhoneNumberManager.getInstance().setMonitorNumber(number);
				textViewNumber.setText(number);

				// The user picked a contact.
				// The Intent's data Uri identifies which contact was selected.

				// Do something with the contact here (bigger example below)
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		Button buttonRequest = ((Button) rootView.findViewById(R.id.buttonRequestCoord));
		Button buttonPickNumber = ((Button) rootView.findViewById(R.id.buttonPickNumber));
		textViewNumber = (TextView) rootView.findViewById(R.id.textViewMonitorNumber);

		buttonRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// StringBuilder sb = new
				// StringBuilder(textViewNumber.getText());

				if (null != PhoneNumberManager.getInstance().getMonitorNumber()) {
					ICUSMS icuSMS = new ICUSMS(PhoneNumberManager.getInstance().getMonitorNumber(), getString(R.string.LOCATION_REQUEST), null);
					
					icuSMS.send();
				}
			}
		});

		buttonPickNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
				pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only
				// phone numbers
				startActivityForResult(pickContactIntent, Requests.PICK_CONTACT_REQUEST);
			}
		});

		SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.PREFERENCES_TARGET_NUMBER), Context.MODE_PRIVATE);
		PhoneNumberManager.getInstance().setMonitorNumber(settings.getString("monitorNumber", ""));
		
		textViewNumber.setText(PhoneNumberManager.getInstance().getMonitorNumber());

		return rootView;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

		SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.PREFERENCES_TARGET_NUMBER), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		editor.putString("monitorNumber", PhoneNumberManager.getInstance().getMonitorNumber());

		editor.commit();

		super.onStop();
	}
}