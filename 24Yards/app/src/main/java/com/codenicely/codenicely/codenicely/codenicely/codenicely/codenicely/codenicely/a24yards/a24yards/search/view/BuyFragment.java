package com.codenicely.codenicely.codenicely.codenicely.codenicely.codenicely.codenicely.a24yards.a24yards.search.view;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.codenicely.codenicely.codenicely.codenicely.codenicely.codenicely.codenicely.a24yards.a24yards.search.presenter.SearchPresenter;
import com.codenicely.codenicely.codenicely.codenicely.codenicely.codenicely.codenicely.a24yards.a24yards.search.presenter.SearchPresenterImpl;
import com.codenicely.codenicely.codenicely.codenicely.codenicely.codenicely.codenicely.a24yards.a24yards.search.provider.RetrofitSearchProvider;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.common.api.GoogleApiClient;


import com.codenicely.codenicely.codenicely.codenicely.codenicely.codenicely.codenicely.a24yards.a24yards.R;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;


import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BuyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyFragment extends Fragment implements  SearchView,GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private List<String> bedroom_list =  new ArrayList<String>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;
    private Button btn_search;
    private CheckBox bhk_1,bhk_2,bhk_3,bhk_4,bhk_5plus;
    private TextView search_txt,min_price,max_price,lacs,crores;
    private CardView card_google_search;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private OnFragmentInteractionListener mListener;
    private String loc,usage_type;
    private int selectedId,minValue_int,maxValue_int,radioid;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private ProgressBar progressBar_buy;

    private SearchPresenter searchPresenter;

    public BuyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyFragment newInstance(String param1, String param2) {
        BuyFragment fragment = new BuyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_buy, container, false);
        initialize(view);
        context = getContext();
        mGoogleApiClient = new GoogleApiClient
                .Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

      //  PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

card_google_search.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(getActivity());
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
            e.printStackTrace();
        }


    }
});
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) view.findViewById(R.id.rangeSeekbar);
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                minValue_int = Integer.parseInt(minValue.toString());
                maxValue_int = Integer.parseInt(maxValue.toString());
                min_price.setText(String.valueOf(minValue_int));
                max_price.setText(String.valueOf(maxValue_int));
                rangeSeekbar.setSteps(5.0f);
                /*
                if (minValue_int >= 100){
                    rangeSeekbar.setSteps(100.0f);
                    lacs.setText("Crores");
                    min_price.setText(String.valueOf(minValue_int/100));
                }
                else{
                    lacs.setText("Lacs");
                    rangeSeekbar.setSteps(5.0f);
                }
                if (maxValue_int >= 100){
                    rangeSeekbar.setSteps(100.0f);
                    max_price.setText(String.valueOf(maxValue_int/100));
                }
                else{
                    rangeSeekbar.setSteps(5.0f);
                    crores.setText("Lacs");
                }
                */
            }
        });

        bhk_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bhk_1.isChecked()){
                    bedroom_list.add(bhk_1.getText().toString());
                }
            }
        });

        bhk_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bhk_2.isChecked()){
                    bedroom_list.add(bhk_2.getText().toString());
                }
            }
        });

        bhk_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bhk_3.isChecked()){
                    bedroom_list.add(bhk_3.getText().toString());
                }
            }
        });

        bhk_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bhk_4.isChecked()){
                    bedroom_list.add(bhk_4.getText().toString());
                }
            }
        });
        bhk_5plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bhk_5plus.isChecked()){
                    bedroom_list.add(bhk_5plus.getText().toString());
                }
            }
        });

         selectedId = radioGroup.getCheckedRadioButtonId();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i==R.id.radio_residential){
                    usage_type = "Residential";
                }else{
                    usage_type = "Commercial";
                }
            }
        });

         //radioButton = (RadioButton) view.findViewById(selectedId);
        /*
        if (selectedId == R.id.radio_residential){
            usage_type = "Residential";
        }
        else if (selectedId == R.id.radio_commercial){
            usage_type = "Commercial";
        }
        */

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               searchPresenter = new SearchPresenterImpl(new RetrofitSearchProvider(),BuyFragment.this);
               searchPresenter.getSearchData("Buy",loc,min_price.getText().toString(),max_price.getText().toString(),bedroom_list,usage_type);
            }
        });


        return view;
    }

    public void initialize(View view){
        bhk_1 = (CheckBox) view.findViewById(R.id.checkbox1_bhk);
        bhk_2 = (CheckBox) view.findViewById(R.id.checkbox2_bhk);
        bhk_3 = (CheckBox) view.findViewById(R.id.checkbox3_bhk);
        bhk_4 = (CheckBox) view.findViewById(R.id.checkbox4_bhk);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        bhk_5plus = (CheckBox) view.findViewById(R.id.checkbox5plus_bhk);
        search_txt = (TextView) view.findViewById(R.id.search_txt);
        min_price = (TextView) view.findViewById(R.id.min_txt);
        max_price = (TextView) view.findViewById(R.id.max_txt);
        lacs = (TextView) view.findViewById(R.id.txt_lacs);
        crores = (TextView) view.findViewById(R.id.txt_crores);
        card_google_search = (CardView)  view.findViewById(R.id.card_search);
        btn_search = (Button) view.findViewById(R.id.search_btn);
        progressBar_buy = (ProgressBar) view.findViewById(R.id.progress_bar_buy);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(getContext(),data);
                Log.i(TAG, "Place: " + place.getName());
                loc = place.getName().toString();
                search_txt.setText(loc);
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar_buy.setVisibility(View.VISIBLE);
        }else{
            progressBar_buy.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showSearchStatus(boolean status) {
        if (status){
            Toast.makeText(getContext(), "Post completed Successfully", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void showError(String message) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
