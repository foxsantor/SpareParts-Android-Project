package com.localparts.projeecto;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String username = "";
    private NavHostFragment mNavHostFragment;
    public static final String SKELETON = "http://41.226.11.252:11853/api/"; //"http://192.168.43.242:5000/api/";
    private static final String URL = SKELETON;
    private static RequestQueue requestQueue;
    private ConstraintLayout container;
    private static boolean res;
    private static Context conx;
    private static boolean  hasConnection = false;
    private FragmentManager fragmentManager;
    boolean isKeyboardShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.container);
        createNotificationChannel();

        conx = this;
        //Navigation
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_market_place, R.id.navigation_bookmarks, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        //response = MainActivity.checekConnection();


        navView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });
        /*ActionBar bar = getSupportActionBar();
        getSupportActionBar().hide();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3B3F42")));*/
        final View view= this.getWindow().getDecorView();
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                                new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onGlobalLayout() {

                                        Rect r = new Rect();
                                        view.getWindowVisibleDisplayFrame(r);
                                        int screenHeight = view.getRootView().getHeight();

                                        // r.bottom is the position above soft keypad or device button.
                                        // if keypad is shown, the r.bottom is smaller than that before.
                                        int keypadHeight = screenHeight - r.bottom;


                                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                                            // keyboard is opened
                                            if (!isKeyboardShowing) {

                                                isKeyboardShowing = true;
                                                navView.setVisibility(View.GONE);

                                            }
                                        }
                                        else {
                                            // keyboard is closed
                                            if (isKeyboardShowing) {
                                                isKeyboardShowing = false;
                                                Transition transition = new Slide(Gravity.BOTTOM);
                                                transition.setDuration(300);
                                                transition.addTarget(navView.getId());
                                                TransitionManager.beginDelayedTransition(container, transition);
                                                navView.setVisibility(View.VISIBLE);

                                            }
                                        }
                    }
                });





    }



    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public  String loadUsername()
    {
        SharedPreferences sharedPreferences=this.getSharedPreferences("share",Context.MODE_PRIVATE);
        username = sharedPreferences.getString("email","");
        return username;
    }
    public static boolean getConnction()
    {

        requestQueue = Volley.newRequestQueue(conx);
        requestQueue.start();
        JsonObjectRequest request = new JsonObjectRequest (Request.Method.GET, SKELETON, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hasConnection =true;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                hasConnection = false;
                return;

            }
        });
        requestQueue.add(request);
        return hasConnection;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "projecto";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}




