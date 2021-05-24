package com.wassemsy.bekity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.HashMap;

import static com.wassemsy.bekity.App.Key_Item;
import static com.wassemsy.bekity.App.Key_ID;
import static com.wassemsy.bekity.App.Key_Title;
import static com.wassemsy.bekity.App.Key_CurrentValue;
import static com.wassemsy.bekity.App.Key_OldValue;

import static com.wassemsy.bekity.App.ListMap;
import static com.wassemsy.bekity.App.ServerLink;
import static com.wassemsy.bekity.App.mListView;
import static com.wassemsy.bekity.App.pDialog;

import static com.wassemsy.bekity.App.LastUpdate;
import static com.wassemsy.bekity.App.Title;
import static com.wassemsy.bekity.App.ImageProduct;
import static com.wassemsy.bekity.App.CurrentValue;
import static com.wassemsy.bekity.App.OldValue;
import static com.wassemsy.bekity.App.ImageStatus;
import static com.wassemsy.bekity.App.ChangePercent;

public class CigarettesFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View fragment_cigarettes = inflater.inflate(R.layout.fragment_home, container, false);

        LastUpdate = fragment_cigarettes.findViewById(R.id.LastUpdate);
        Title = fragment_cigarettes.findViewById(R.id.Title);
        ImageProduct = fragment_cigarettes.findViewById(R.id.ImageProduct);
        CurrentValue = fragment_cigarettes.findViewById(R.id.CurrentValue);
        OldValue = fragment_cigarettes.findViewById(R.id.OldValue);
        ImageStatus = fragment_cigarettes.findViewById(R.id.ImageStatus);
        ChangePercent = fragment_cigarettes.findViewById(R.id.ChangePercent);
        mListView = fragment_cigarettes.findViewById(R.id.listView);
        // SetFonts();
        new GetItems().execute();
       //  SetChange();

        return fragment_cigarettes;
    }

    @SuppressLint("StaticFieldLeak")
    private class GetItems extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Loading ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        public Void doInBackground(Void... voids) {
            try {
                ListMap = new ArrayList<>();
                XMLParser parser = new XMLParser();
                NodeList nl =
                        parser.getDomElement(parser
                                .getXmlFromUrl(ServerLink))
                                .getElementsByTagName(Key_Item);
                for (int i = 0; i < nl.getLength(); i++) {
                    HashMap<String, String> HashMap = new HashMap<>();
                    Element e = (Element) nl.item(i);

                    HashMap.put(Key_ID, parser.getValue(e, Key_ID));
                    HashMap.put(Key_Title, parser.getValue(e, Key_Title));
                    //   map.put(Key_ImageProduct, parser.getValue(e, Key_ImageProduct));
                    HashMap.put(Key_CurrentValue, parser.getValue(e, Key_CurrentValue));
                    HashMap.put(Key_OldValue, parser.getValue(e, Key_OldValue));

                    ListMap.add(HashMap);
                }
                return null;
            } catch (Exception e) {
                if (pDialog == null || !pDialog.isShowing()) {
                    Log.e("Bekity3: ", e.getMessage());
                    return null;
                }
                pDialog.dismiss();
                Log.e("Bekity4: ", e.getMessage());
                return null;
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            } catch (Exception ignored) { }
            if (!App.isOnline(requireContext())) {
                App.ShowToast(R.string.wassem_no_internet, requireContext());
                return;
            }
            // display the items
            ListAdapter adapter =
                    new SimpleAdapter(
                            getContext(), ListMap , R.layout.box_grid_element,
                            new String[] {Key_Title, Key_CurrentValue, Key_OldValue},
                            new int[] {R.id.Title, R.id.CurrentValue, R.id.OldValue}
                    );
            mListView.setAdapter(adapter);
            if (mListView==null){
                App.ShowToast(R.string.wassem_error, requireContext());
            } else {
                App.ShowToast(R.string.wassem_last_update, requireContext());
            }
           mListView.setOnTouchListener((view, motionEvent) -> false);
        }

    }

}
