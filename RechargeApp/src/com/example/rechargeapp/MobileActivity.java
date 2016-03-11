package com.example.rechargeapp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rechargeapp.parser.JSONParser;

public class MobileActivity extends Fragment implements OnItemSelectedListener, OnClickListener {


	private static final String MyPREFERENCES = "LoginSession";
	SharedPreferences sharedpreferences;
	private static String url = "http://10.251.163.5:8088/MFRPServices/recharge_get_number";
	ProgressDialog pDialog;
	Button btproceed;
	String userid;
	JSONArray jsonarray;
	String provider_array[];
	String jsonarray_new[]=null;
	Spinner mySpinner;
	EditText provider,amount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_mobile, container, false);
		mySpinner = (Spinner) rootView.findViewById(R.id.mobile_number_spinner);
		provider = (EditText)rootView.findViewById(R.id.mobile_provider_edit);
		provider.setEnabled(false);
		amount = (EditText)rootView.findViewById(R.id.mobile_amount_edit);
		btproceed = (Button)rootView.findViewById(R.id.mobile_button);
		//sciencelist = (ListView)rootView.findViewById(R.id.science_list);
		sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		userid = sharedpreferences.getString("userKey", "");
		Toast.makeText(this.getActivity(), userid, Toast.LENGTH_LONG).show();
		new mobile().execute();
		mySpinner.setOnItemSelectedListener(this);
		btproceed.setOnClickListener(this);
		return rootView;
	}



	/**
	 * Background Async Task to Create new product
	 * */
	class mobile extends AsyncTask<String, String, String[]> {
		private static final String TAG = "";
		//private static final String TAG_SUCCESS = "registered";
		JSONParser jsonParser = new JSONParser();

		/**
		 * Creating product
		 * */
		protected String[] doInBackground(String... args) {
			try{

				String json = "";

				// 3. build jsonObject
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("user_id", userid);


				// 4. convert JSONObject to JSON to String
				json = jsonObject.toString();

				// getting JSON Object
				// Note that create product url accepts POST method
				JSONObject json1 = jsonParser.makeHttpRequest(url,
						"POST", json);

				// check log cat fro response
				Log.d("Create Response", json1.toString());

				Log.i(TAG, ""+json1.toString());
				int j=0;
				int length_array=0;
				jsonarray = json1.getJSONArray("recharge_number");
				for(int i=0;i<jsonarray.length();i++){
					JSONObject jsonresult  = jsonarray.getJSONObject(i);

					String mtype = jsonresult.getString("type");
					String number = jsonresult.getString("number");
					if(mtype.equals("mobile")){
						if(((number.length())==10) && number != null){
							length_array++;
						}
					}
				}
				jsonarray_new=new String[length_array];
				provider_array=new String[length_array];
				for(int i=0;i<jsonarray.length();i++){

					JSONObject jsonresult  = jsonarray.getJSONObject(i);
					// Do something with phone data

					String mtype = jsonresult.getString("type");

					if(mtype.equals("mobile")){
						String number = jsonresult.getString("number");
						if(((number.length())==10) && number != null){
							jsonarray_new[j]=number;

							provider_array[j]=jsonresult.getString("provider_name");

							Log.i(TAG, ""+jsonarray_new[j]);
							Log.i(TAG, ""+provider_array[j]);
							j++;
						}
						//System.out.println(jsonarray_new);
					}
				}
				return jsonarray_new;

			}
			catch(Exception e)
			{

			}
			// Log.i("in postexe mobile", "ide"+jsonarray_new);


			return jsonarray_new;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String[] result) {
			// dismiss the dialog once done
			//pDialog.dismiss();

			Log.i("in postexe mobile", "ide"+result.length);
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, result);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
			mySpinner.setAdapter(dataAdapter);
			provider.setText(provider_array[0]);  


		}

	}



	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

		provider.setText(provider_array[pos]);



	}



	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}



	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent rechargepaymentintent=new Intent(getActivity(),RechargePaymentMainActivity.class);
		rechargepaymentintent.putExtra("amount", amount.getText().toString());
		rechargepaymentintent.putExtra("mobilenumber", mySpinner.getSelectedItem().toString());
		startActivity(rechargepaymentintent);
	}







}
