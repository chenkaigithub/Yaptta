package edu.ece.ncsu.unofficial.yaptta;

import edu.ece.ncsu.unofficial.yaptta.R;
import edu.ece.ncsu.unofficial.yaptta.core.YapttaConstants;
import edu.ece.ncsu.unofficial.yaptta.core.YapttaState;
import edu.ece.ncsu.unofficial.yaptta.core.callbacks.CoreServiceCallback;
import edu.ece.ncsu.unofficial.yaptta.core.conduits.ConduitException;
import edu.ece.ncsu.unofficial.yaptta.core.conduits.MulticastConduit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize the core conduit (only once)
        MulticastConduit coreConduit = YapttaState.getCoreConduit();
        if(coreConduit == null) {
        	coreConduit = new MulticastConduit(YapttaConstants.Network.BROADCAST_PORT);
        	YapttaState.setCoreConduit(coreConduit);
        }
        
        try {
			coreConduit.startListening(new CoreServiceCallback(coreConduit));
		} catch (ConduitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Recall the necessary settings
        SharedPreferences settings = getSharedPreferences(YapttaConstants.UI.PREFERENCE_STORE_NAME, Context.MODE_PRIVATE);
        String deviceName = settings.getString(YapttaConstants.Preferences.DEVICE_NAME, "Yaptta Device");
        YapttaState.setDeviceName(deviceName);
        
        // ... and then update UI elements accordingly
        findEditDeviceName().setText(deviceName);
        
    }
    
    /**
     * Convenience method for accessing the device name text box.
     * @return Instance of the device name text box
     */
    private TextView findEditDeviceName() {
    	return (TextView)findViewById(R.id.editDeviceName);
    }
    
    /**
     * Click handler for saving the device name preference.
     * @param view 
     */
    public void onSaveDeviceName(View view) {
    	SharedPreferences settings = getSharedPreferences(YapttaConstants.UI.PREFERENCE_STORE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(YapttaConstants.Preferences.DEVICE_NAME, findEditDeviceName().getText().toString());

        // Commit the edits!
        editor.commit();
	}

    /**
     * Click handler for joining a group.
     * @param view
     */
    public void onJoinClick (View view) {
    	Intent i = new Intent(getApplicationContext(), JoinGroupActivity.class);
    	startActivity(i);
	}

    /**
     * Click handler for starting a new group.
     * @param view
     */
    public void onStartClick (View view) {
    	Intent i = new Intent(getApplicationContext(), StartActivity.class);
    	startActivity(i);
	}
    
    /**
     * Overridden so that the back button completely closes the application.
     */
    @Override
    public void onBackPressed() {
    	this.finish();
    }
    
}
