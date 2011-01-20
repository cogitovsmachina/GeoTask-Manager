package org.androidtitlan.geotaskmanager;

import org.androidtitlan.geotaskmanager.R;
import org.androidtitlan.geotaskmanager.tasks.Task;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddTaskActivity extends TaskManagerActivity{
	private static final int REQUEST_CHOOSE_ADRESS = 0;
	private Object taskNameEditText;
	private Button addButton;
	private Button cancelButton;
	protected boolean changesPending;
	private AlertDialog unsavedChangesDialog;
	private Address address;
	private Button addLocationButton;
	private TextView addressText;
	private AlertDialog theTaskMustHaveDescriptionDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_tasks);
		setUpViews();
		}
	
	@Override
	protected void onResume(){
		super.onResume();
		if (null == address){
			addLocationButton.setVisibility(View.VISIBLE);
			addressText.setVisibility(View.GONE);
		} else{
			addLocationButton.setVisibility(View.GONE);
			addressText.setVisibility(View.VISIBLE);
			addressText.setText(address.getAddressLine(0));
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//An easy way to obtain Address of a given MapActivity using this simple sentence =)
		if (REQUEST_CHOOSE_ADRESS == requestCode && RESULT_OK == resultCode){
			address = data.getParcelableExtra(AddLocationMapActivity.ADDRESS_RESULT);
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	protected void addTask() {
		String taskName = ((TextView) taskNameEditText).getText().toString();
		//validating if editText empty
		if(taskName.equals("")){
			showTaskMustHaveDescriptionDialog();
		}
		else{
			Task t = new Task(taskName);
		t.setAdress(address);
		getGeoTaskManagerApplication().addTask(t);
		finish();
			
	}		
	}
	
		
	//Using this method to tell the user need to add something to the task title =)
	private void showTaskMustHaveDescriptionDialog() {
		theTaskMustHaveDescriptionDialog = new AlertDialog.Builder(this)
		.setTitle(R.string.the_task_must_have_description_title)
		.setMessage(R.string.the_task_must_have_description_message)
		.setNeutralButton(R.string.ok_ill_never_doit_again, new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				}
		})	
		.create();
	theTaskMustHaveDescriptionDialog.show();		
	}

	//Here we are using another way of Click Listener, which basically uses XML Layout to call a method =).
	public void addLocationButtonClicked(View view){
    	   Intent intent = new Intent(AddTaskActivity.this, AddLocationMapActivity.class);
   		//we use startActivityForResult when we need to get a responseData to the class we entered.
   		startActivityForResult(intent, REQUEST_CHOOSE_ADRESS);

       }        

		
	

	protected void cancel() {
		if (changesPending) {
			unsavedChangesDialog = new AlertDialog.Builder(this)
				.setTitle(R.string.unsaved_changes_title)
				.setMessage(R.string.unsaved_changes_message)
				.setPositiveButton(R.string.add_task, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						addTask();
					}

				})
				.setNeutralButton(R.string.discard, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						unsavedChangesDialog.cancel();
					}
				})
				.create();
			unsavedChangesDialog.show();
		} else {
			finish();
		}
	}


	private void setUpViews() {
		taskNameEditText =(EditText)findViewById(R.id.task_name);
		addButton =(Button)findViewById(R.id.add_button);
		cancelButton = (Button)findViewById(R.id.cancel_button);
		addLocationButton = (Button)findViewById(R.id.add_location_button);
		addressText = (TextView)findViewById(R.id.address_text);
 		
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addTask();
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { 
				cancel();
			}
		});
		
		//Here we are adding a TextWatcher, just to validate there are some
		//text inside our taskNameEditText EditText 
		((TextView) taskNameEditText).addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				changesPending =true;				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}
			
			public void afterTextChanged(Editable s) {
				
			}
		});
	

	}
		
	
}
