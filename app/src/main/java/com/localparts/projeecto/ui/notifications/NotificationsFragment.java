package com.localparts.projeecto.ui.notifications;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.localparts.projeecto.MainActivity;
import com.localparts.projeecto.adapters.CategoryAdapter;
import com.localparts.projeecto.entities.CategoryItems;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.localparts.projeecto.R;
import com.localparts.projeecto.adapters.sellsAdapter;
import com.localparts.projeecto.entities.Parts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private static final String URL = MainActivity.SKELETON+"parts/getSells";
    private static final String URL_Vues = MainActivity.SKELETON+"parts/UpdateVues";
    private RecyclerView mRecyclerView;
    private ArrayList<Parts> mSellsList;
    private RequestQueue mRequestQueue;
    private sellsAdapter mSellsAdapter;
    private GridView gridView;
    private NestedScrollView scrolli,scroll2;
    private ImageButton toggler,close,toggler2,restart;
    private CardView die;
    private String username;
    private ConstraintLayout validView,Refresh,loading,helper,DealsView,register,popup,wrong,auctionView;
    private Button join,viewAll,viewAll2;
    private AnimatorSet mAnimationSet;
    private CategoryAdapter categoryAdapter;
    private MaterialButton materialButton;
    private ArrayList<CategoryItems> categoryItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        username = loadUsername();
        scrolli = root.findViewById(R.id.nani);
        scroll2 = root.findViewById(R.id.nani2);
        restart = root.findViewById(R.id.restart);
        close = root.findViewById(R.id.close);
        loading = root.findViewById(R.id.loading_1);
        helper = root.findViewById(R.id.helper);
        gridView = root.findViewById(R.id.grid);
        wrong = root.findViewById(R.id.wrong);
        toggler2 = root.findViewById(R.id.hide2);
        wrong.setVisibility(View.GONE);
        auctionView = root.findViewById(R.id.auctionView);
        viewAll = root.findViewById(R.id.viewall);
        viewAll2 = root.findViewById(R.id.viewall2);
        popup = root.findViewById(R.id.popup);
        toggler = root.findViewById(R.id.hide);
        DealsView = root.findViewById(R.id.DealsView);
        validView = root.findViewById(R.id.bigView);
        validView.setVisibility(View.GONE);
        mRecyclerView = root.findViewById(R.id.Sells);
        die = root.findViewById(R.id.relative_layout_id);
        materialButton = root.findViewById(R.id.materialButton);
        register = root.findViewById(R.id.register);
        join = root.findViewById(R.id.join);
        categoryItems = new ArrayList<>();
        mSellsList = new ArrayList<>();
        categoryItems = Poulater();
        categoryAdapter = new CategoryAdapter(getActivity(),categoryItems);
        gridView.setAdapter(categoryAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRequestQueue = Volley.newRequestQueue(getContext());




        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(die, "alpha", .5f, .1f);
        fadeOut.setDuration(300);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(die, "alpha", .1f, .5f);
        fadeIn.setDuration(300);
        mAnimationSet = new AnimatorSet();
        mAnimationSet.play(fadeIn).after(fadeOut);
        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                CategoryItems cat = categoryItems.get(position);
                Bundle data = new Bundle();
                data.putString("key",cat.getName());
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.categoryView,data);
            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("type","Available Deals");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.viewAll,data);

            }
        });
        viewAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("type","Current Running Auctions");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.viewAll,data);

            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.navigation_account);
            }
        });
        if(username.equals(""))
        {
            register.setVisibility(View.VISIBLE);
        }else
        {
            register.setVisibility(View.GONE);
        }

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
                    @Override
                    public boolean canScrollVertically() {
                        return  false;
                    }
                });

                scrolli.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

                scroll2.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

                popup.setVisibility(View.VISIBLE);
            }
        });



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.setVisibility(View.GONE);
                scrolli.setOnTouchListener(null);
                scroll2.setOnTouchListener(null);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        toggler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DealsView.getVisibility() == View.VISIBLE)
                {
                    DealsView.setVisibility(View.GONE);
                    toggler.setImageResource(R.drawable.ic_navigate_next_black_24dp);
                }else
                {
                    DealsView.setVisibility(View.VISIBLE);
                    toggler.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                }
            }
        });
        toggler2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auctionView.getVisibility() == View.VISIBLE)
                {
                    auctionView.setVisibility(View.GONE);
                    toggler2.setImageResource(R.drawable.ic_navigate_next_black_24dp);
                }else
                {
                    auctionView.setVisibility(View.VISIBLE);
                    toggler2.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                }
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                        Navigation.findNavController(v).navigate(R.id.notificationsFragment);
            }
        });

        GetSells();
        return root;
    }

    public  String loadUsername()
    {
        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences("share", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("email","");
        return username;
    }



    private void GetSells() {

        loading.setVisibility(View.VISIBLE);
        validView.setVisibility(View.GONE);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            int length = 0;
                            if (response.length() > 3){
                                length = 3;}
                            else{
                                length = response.length();
                                }
                            for (int i = 0; i < length; i++) {


                                JSONObject hit = response.getJSONObject(i);
                                int id = hit.getInt("idparts");
                                String owner = hit.getString("owner");
                                String name = hit.getString("name");
                                String other1 = hit.getString("other1");
                                Float price = Float.parseFloat(hit.getString("Price"));
                                String other2 = hit.getString("other2");
                                String other3 = hit.getString("other3");
                                String tag_description = hit.getString("tag_description");
                                String created = hit.getString("sellable_date");
                                String type = hit.getString("Type");
                                String state = hit.getString("state");
                                String images = hit.getString("String_image");
                                String ref = hit.getString("refrence");
                                int vues = hit.getInt(("vues"));
                                byte[] decodedString = Base64.decode(images, Base64.DEFAULT);
                                if(!state.equals("SOLD"))
                                mSellsList.add(new Parts(id,owner,name,other1,price,other2,other3,type,state,decodedString,tag_description,created,ref,vues));

                            }

                            mSellsAdapter = new sellsAdapter(getActivity(), mSellsList, new sellsAdapter.DetailsAdapterListener() {
                                @Override
                                public void DetailOnClick(View v, int position) {
                                    Bundle data = new Bundle();
                                    data.putString("owner",mSellsList.get(position).getOwner());
                                    data.putInt("idparts",mSellsList.get(position).getId());
                                    data.putString("state",mSellsList.get(position).getState());
                                    data.putString("name",mSellsList.get(position).getName());
                                    data.putString("other1",mSellsList.get(position).getOther1());
                                    data.putString("other2",mSellsList.get(position).getOther2());
                                    data.putString("other3",mSellsList.get(position).getOther3());
                                    data.putString("Type",mSellsList.get(position).getType());
                                    data.putByteArray("image",mSellsList.get(position).getImage());
                                    data.putString("refrnce",mSellsList.get(position).getRefrence());
                                    data.putFloat("price",mSellsList.get(position).getPrice());
                                    data.putString("tag_description",mSellsList.get(position).getTag_desc());
                                    data.putString("Created",mSellsList.get(position).getCreated());
                                    data.putInt("vues",mSellsList.get(position).getVues());
                                    UpdateVues(String.valueOf(mSellsList.get(position).getId()));

                                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.viewalone,data);

                                }
                            });
                            mRecyclerView.setAdapter(mSellsAdapter);
                            final Handler handlers = new Handler();
                            handlers.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    wrong.setVisibility(View.GONE);
                                    loading.setVisibility(View.GONE);
                                    validView.setVisibility(View.VISIBLE);

                                }
                            }, 1000);
                        } catch (JSONException e) {
                            loading.setVisibility(View.GONE);
                            validView.setVisibility(View.GONE);
                            restart.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            final Handler handler = new Handler();
                validView.setVisibility(View.GONE);
                wrong.setVisibility(View.VISIBLE);
                restart.setVisibility(View.INVISIBLE);

            handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.GONE);
                restart.setVisibility(View.VISIBLE);
            }
        }, 2000);
                error.printStackTrace();

            }
        });

        mRequestQueue.add(request);
    }
    private ArrayList<CategoryItems> Poulater()
    {
        ArrayList<CategoryItems> categoryItems = new ArrayList<>();
        categoryItems.add(new CategoryItems("Brakes",R.drawable.ibreak));
        categoryItems.add(new CategoryItems("Filtering/Oil",R.drawable.ioil));
        categoryItems.add(new CategoryItems("Suspension and Steering",R.drawable.isuspension));
        categoryItems.add(new CategoryItems("Transmission-Gearbox",R.drawable.igearshift));
        categoryItems.add(new CategoryItems("Accessories",R.drawable.iseatbelt));
        categoryItems.add(new CategoryItems("Engine compartment",R.drawable.iengine));
        categoryItems.add(new CategoryItems("Exhaust",R.drawable.iexhaust));
        categoryItems.add(new CategoryItems("Air conditioning",R.drawable.iairconditioner));
        categoryItems.add(new CategoryItems("Electrical and lighting",R.drawable.ilights));
        categoryItems.add(new CategoryItems("Locks-closures",R.drawable.icarkey));
        categoryItems.add(new CategoryItems("Tyres",R.drawable.itire));
        categoryItems.add(new CategoryItems("Others",R.drawable.ialloywheel));
        return categoryItems;
    }

    private void UpdateVues(String idparts)
    {

        HashMap< String, String > params = new HashMap<String, String>();
        params.put("idparts",  idparts);

        // the entered data as the JSON body.
        JsonObjectRequest jsObjRequest = new
                JsonObjectRequest(Request.Method.PUT,
                URL_Vues,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Connection Lost "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        mRequestQueue.add(jsObjRequest);
    }




}