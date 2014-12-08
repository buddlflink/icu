package matze.gps.icu;

import matze.gps.icu.model.ICUSMS;
import matze.gps.icu.model.Requests;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static MainFragment newInstance(int sectionNumber) {
		MainFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		Button b = ((Button) rootView.findViewById(R.id.buttonRequestCoord));

		// if(null != b)
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ICUSMS icuSMS = new ICUSMS(MainActivity.phoneNumberManager.getTargetPhonenumber(), Requests.LOCATION_REQUEST, null);

				icuSMS.send();
			}
		});

		return rootView;
	}
}