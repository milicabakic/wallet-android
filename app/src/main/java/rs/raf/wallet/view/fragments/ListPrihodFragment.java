package rs.raf.wallet.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rs.raf.wallet.R;
import rs.raf.wallet.model.Prihod;
import rs.raf.wallet.view.activities.EditFinanceActivity;
import rs.raf.wallet.view.activities.FinanceActivity;
import rs.raf.wallet.view.recycler.adapter.PrihodAdapter;
import rs.raf.wallet.view.recycler.differ.PrihodDiffer;
import rs.raf.wallet.view_model.RecyclerViewModelPrihod;

public class ListPrihodFragment extends Fragment {

    public static final int PRIHOD_RECEIVED_REQUEST_CODE = 111;
    public static final int EDIT_PRIHOD_RECEIVED_REQUEST_CODE = 112;
    public static final String EDIT_PRIHOD_RECEIVED_MESSAGE = "editFinanceReceivedKey";

    RecyclerViewModelPrihod recyclerViewModel;
    PrihodAdapter adapter;

    //View components
    RecyclerView recyclerView;
    View view;


    public ListPrihodFragment(){
        super(R.layout.fragment_prihodi);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewModel = new ViewModelProvider(getActivity()).get(RecyclerViewModelPrihod.class);
        this.view = view;
        init(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        init(view);
    }

    private void init(View view){
        initView(view);
        initListeners(view);
        initRecycler(view);
        initObservers(view);
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.listPrihodi);
    }

    private void initListeners(View view){

    }

    private void initObservers(View view) {
        recyclerViewModel.getPrihodi().observe(getViewLifecycleOwner(), prihodi -> {
            adapter.submitList(prihodi);
        });
    }

    private void initRecycler(View view) {
        adapter = new PrihodAdapter(new PrihodDiffer(), prihod -> {
            Intent intent = new Intent(getActivity(), FinanceActivity.class);
            intent.setAction(FinanceActivity.ACTION_PRIHOD);
            intent.putExtra(FinanceActivity.PRIHOD_KEY, prihod);
            startActivityForResult(intent, PRIHOD_RECEIVED_REQUEST_CODE);
            return null;
        }, prihodDelete -> {
            recyclerViewModel.deletePrihod(prihodDelete.getId());
            return null;
        }, prihodEdit -> {
            Intent intent = new Intent(getActivity(), EditFinanceActivity.class);
            intent.setAction(EditFinanceActivity.ACTION_PRIHOD);
            intent.putExtra(EditFinanceActivity.PRIHOD_KEY, prihodEdit);
            startActivityForResult(intent, EDIT_PRIHOD_RECEIVED_REQUEST_CODE);
            return null;
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDIT_PRIHOD_RECEIVED_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    Prihod prihod = (Prihod) data.getExtras().getSerializable(EDIT_PRIHOD_RECEIVED_MESSAGE);

                    recyclerViewModel.editPrihod(prihod);
                    adapter.notifyDataSetChanged();

                }
                break;
        }
    }
}
