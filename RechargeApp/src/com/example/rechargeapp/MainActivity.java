

package com.example.rechargeapp;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rechargeapp.parser.JSONParser;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	public static final String MyPREFERENCES = "LoginSession" ;
	// url to create new product
	private static String url = "http://10.251.163.5:8088/MFRPServices/login";
	SharedPreferences sharedpreferences;
	ProgressDialog pDialog;
	EditText etusername,etpassword;
	Button btlogin;
	static final String user_id = "userKey";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		etusername=(EditText)findViewById(R.id.login_user_id_edit);
		etpassword=(EditText)findViewById(R.id.login_password_edit);
		btlogin=(Button)findViewById(R.id.login_button);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		btlogin.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Background Async Task to Create new product
	 * */
	class login extends AsyncTask<String, String, Boolean> {
		//private static final String TAG_SUCCESS = "registered";
		JSONParser jsonParser = new JSONParser();

		private static final String TAG = "login";

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Attempting for login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected Boolean doInBackground(String... args) {
			try{
				String username = etusername.getText().toString();
				String password=etpassword.getText().toString();

				String json = "";

				// 3. build jsonObject
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("user_id", username);
				jsonObject.accumulate("password", password);


				// 4. convert JSONObject to JSON to String
				json = jsonObject.toString();

				// getting JSON Object
				// Note that create product url accepts POST method
				JSONObject json1 = jsonParser.makeHttpRequest(url,
						"POST", json);

				// check log cat fro response
				Log.d("Create Response", json1.toString());

				Log.i(TAG, ""+json1.toString());
				String success = json1.getString("status");
				if(success.equals("Success"))
					return true;

			}
			catch(Exception e)
			{

			}

			return false;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(Boolean result) {
			// dismiss the dialog once done
			pDialog.dismiss();

			if(result)
			{
				String n  = etusername.getText().toString();


				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString(user_id, n);
				editor.commit();

				Intent i=new Intent(MainActivity.this,HomeActivity.class);
				startActivity(i);

			}
			else
			{
				Toast.makeText(getApplicationContext(), "Invalid user id or password", Toast.LENGTH_SHORT).show();
			}


		}

	}


	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		new login().execute();

	}

}
