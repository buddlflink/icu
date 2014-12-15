package matze.gps.icu.monitor;

import matze.gps.icu.R;
import matze.gps.icu.control.PhoneNumberManager;
import matze.gps.icu.model.Requests;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class RemoteFragment extends Fragment {

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
//	private static final String ARG_SECTION_NUMBER = "section_number";
	private static RemoteFragment fragment;

	LinearLayout layout;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static RemoteFragment newInstance() {
		fragment = new RemoteFragment();

		// Bundle args = new Bundle();
		// args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		// fragment.setArguments(args);
		return fragment;
	}

	public static RemoteFragment getFragment() {
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_remote, container, false);

		Button buttonAddNummber = (Button) rootView.findViewById(R.id.buttonAddNumber);
		layout = (LinearLayout) rootView;

		buttonAddNummber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
				pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only
				// phone numbers
				startActivityForResult(pickContactIntent, Requests.PICK_CONTACT_REQUEST);
			}
		});

		SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.PREFERENCES_AUT_NUMBERS), Context.MODE_PRIVATE);
		
		
		for (String n : settings.getAll().keySet()) {
			PhoneNumberManager.getInstance().addAuthorizedNumber(n);
			TextView newNumber = new TextView(getActivity());
			newNumber.setText(n);
			layout.addView(newNumber);
		}
		

		return rootView;
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
		 SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.PREFERENCES_AUT_NUMBERS), Context.MODE_PRIVATE);
	      SharedPreferences.Editor editor = settings.edit();
	      for(String n :  PhoneNumberManager.getInstance().getAuthorizedNumbers()){
	    	  editor.putString(n, "authorizedNumber");
	    	  
	      }
	      editor.commit();
		
		super.onStop();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == Requests.PICK_CONTACT_REQUEST) {
			// Make sure the request was successful
			if (resultCode == -1) {

				// Log.i("debug", "resultCode " + resultCode);
				// Log.i("debug", "resultCode " + data.getPackage());

				Uri contactUri = data.getData();

				String[] projection = { Phone.NUMBER };

				Cursor cursor = getActivity().getContentResolver().query(contactUri, projection, null, null, null);
				cursor.moveToFirst();

				int column = cursor.getColumnIndex(Phone.NUMBER);
				String number = cursor.getString(column);

				TextView newNumber = new TextView(getActivity());
				newNumber.setText(number);
				layout.addView(newNumber);
				PhoneNumberManager.getInstance().addAuthorizedNumber(number);

				// The user picked a contact.
				// The Intent's data Uri identifies which contact was selected.

				// Do something with the contact here (bigger example below)
			}
		}
	}

}