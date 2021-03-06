package matze.gps.icu.monitor;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.control.ObserverManager;
import matze.gps.icu.model.ICUSMS;
import matze.gps.icu.model.Observed;
import matze.gps.icu.model.Requests;
import matze.gps.icu.model.Settings;
import android.app.Fragment;
import android.content.Intent;
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
import android.widget.CheckBox;
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
	Settings settings = null;
	ObserverManager observerManager;

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

				Uri contactUri = data.getData();

				String[] projection = { Phone.NUMBER };

				Cursor cursor = getActivity().getContentResolver().query(contactUri, projection, null, null, null);
				cursor.moveToFirst();

				int column = cursor.getColumnIndex(Phone.NUMBER);
				String number = cursor.getString(column);

				
				boolean inList = false;
				for (Observed o : observerManager.getAllObserved()) {
					inList = inList || (o.getNumber().equals(number));
				}

				if (!inList) {
					
					textViewNumber.setText(number);

					Observed other = new Observed(false, 'U',number);
					observerManager.addObserved(other);

				}

			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		if (null == observerManager){
			observerManager= ((MainActivity)getActivity()).getPhoneNumberManager();
		}
		
		// (MainActivity) getActivity())
		settings = ((MainActivity) getActivity()).getSettings();

		Log.i("debug", "rootview " + rootView.getClass().getSimpleName());

		Button buttonPickNumber = ((Button) rootView.findViewById(R.id.buttonPickNumber));
		textViewNumber = (TextView) rootView.findViewById(R.id.textViewMonitorNumber);
		TextView localCoord = (TextView) rootView.findViewById(R.id.textViewLocalCoord);

		Button buttonRequest = ((Button) rootView.findViewById(R.id.buttonRequestCoord));
		buttonRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// StringBuilder sb = new
				// StringBuilder(textViewNumber.getText());

				for(Observed o: observerManager.getAllObserved()){
					if(!o.isMe()){
						ICUSMS icuSMS = new ICUSMS(o.getNumber(), getString(R.string.LOCATION_REQUEST), null, "");
						icuSMS.send();
					}
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

		CheckBox checkBoxDisplay = (CheckBox) rootView.findViewById(R.id.checkBoxDisplay);

		checkBoxDisplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settings.setDisplayAlwaysOn(((CheckBox) v).isChecked());
			}
		});

		checkBoxDisplay.setChecked(settings.isDisplayAlwaysOn());

		// @todo: list of numbers
		for(Observed o: observerManager.getAllObserved()){
			if(!o.isMe()){
				textViewNumber.setText(o.getNumber());
			}
		}
		
		localCoord.setText("last known: " + observerManager.getMe().getPosition());

		return rootView;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

		super.onStop();
	}
}