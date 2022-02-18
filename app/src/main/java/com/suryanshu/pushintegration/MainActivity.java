package com.suryanshu.pushintegration;

import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.clevertap.android.pushtemplates.PushTemplateNotificationHandler;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.clevertap.android.sdk.interfaces.NotificationHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CTInboxListener, DisplayUnitListener {

    TextView title,mssg;
    CardView c;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.b1);
        Button button2 = findViewById(R.id.b2);
        Button inboxButton = findViewById(R.id.b3);
        Button getmsg = findViewById(R.id.b4);
        Button nativeDisplay=findViewById(R.id.b5);
         c=findViewById(R.id.c1);
        title=findViewById(R.id.native_display_title);
        mssg=findViewById(R.id.native_display_message);

        CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());

        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);

        CleverTapAPI.createNotificationChannel(getApplicationContext(), "36", "Channel36", "ChannelDescription", NotificationManager.IMPORTANCE_MAX, true);

        CleverTapAPI.setNotificationHandler((NotificationHandler) new PushTemplateNotificationHandler());

        CleverTapAPI.getDefaultInstance(this).setDisplayUnitListener(this);

        CleverTapAPI cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);

        if (cleverTapDefaultInstance != null) {
            //Set the Notification Inbox Listener
            cleverTapDefaultInstance.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            cleverTapDefaultInstance.initializeInbox();
        }



            button1.setOnClickListener(v -> {
                HashMap<String, Object> profileUpdate = new HashMap<>();
                profileUpdate.put("Name", "Suryanshu Raj");    // String
                // profileUpdate.put("Identity", 61026032);      // String or number
                profileUpdate.put("Email", "test@test.com"); // Email address of the user
                profileUpdate.put("Phone", "+14155551235");   // Phone (with the country code, starting with +)
                profileUpdate.put("Gender", "M");             // Can be either M or F
                profileUpdate.put("DOB", new Date());         // Date of Birth. Set the Date object to the appropriate value first
                // optional fields. controls whether the user will be sent email, push etc.
                profileUpdate.put("MSG-email", true);        // Disable email notifications
                profileUpdate.put("MSG-push", true);          // Enable push notifications
                profileUpdate.put("MSG-sms", true);          // Disable SMS notifications
                profileUpdate.put("MSG-whatsapp", true);      // Enable WhatsApp notifications
                ArrayList<String> stuff = new ArrayList<>();
                stuff.add("bag");
                stuff.add("shoes");
                profileUpdate.put("MyStuff", stuff);                        //ArrayList of Strings
                String[] otherStuff = {"Jeans", "Perfume"};
                profileUpdate.put("MyStuff", otherStuff);                   //String Array
                assert clevertapDefaultInstance != null;
                clevertapDefaultInstance.onUserLogin(profileUpdate);
            });


        button2.setOnClickListener(view -> clevertapDefaultInstance.pushEvent("suryanshu"));

        getmsg.setOnClickListener(v -> { clevertapDefaultInstance.pushEvent("suryanshugetmsg"); });

       // CleverTapAPI.getDefaultInstance(this).pushEvent("suryanshunativedisplay");
        nativeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clevertapDefaultInstance.pushEvent("NativeDsiplaySuryanshu");
//                onDisplayUnitsLoaded();
            }
        });
        inboxButton.setOnClickListener(v -> { cleverTapDefaultInstance.showAppInbox(); });

        }

    @Override
    public void inboxDidInitialize() {

    }

    @Override
    public void inboxMessagesDidUpdate() {

    }

    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {

        //public CleverTapDisplayUnit getDisplayUnitForId();
        for (int i=0;i<units.size();i++) {
            CleverTapDisplayUnit unit = units.get(i);
            System.out.println(unit);
            prepareDisplayView(unit);
        }
    }
    private void prepareDisplayView(CleverTapDisplayUnit unit) {

        for(CleverTapDisplayUnitContent i:unit.getContents()) {
            title.setText(i.getTitle());
            mssg.setText(i.getMessage());

        }

        //Notification Viewed Event
        CleverTapAPI.getDefaultInstance(this).pushDisplayUnitViewedEventForID(unit.getUnitID());

        //Notification Clicked Event
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CleverTapAPI.getDefaultInstance(getApplicationContext()).pushDisplayUnitClickedEventForID(unit.getUnitID());

            }
        });
    }
}

