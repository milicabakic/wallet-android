package rs.raf.wallet.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rs.raf.wallet.R;
import rs.raf.wallet.model.Prihod;
import rs.raf.wallet.model.Rashod;
import rs.raf.wallet.view.activities.EditFinanceActivity;
import rs.raf.wallet.view.activities.FinanceActivity;
import rs.raf.wallet.view.recycler.adapter.RashodAdapter;
import rs.raf.wallet.view.recycler.differ.RashodDiffer;
import rs.raf.wallet.view_model.RecyclerViewModelRashod;

public class ListRashodFragment extends Fragment {

    public static final int RECEIVED_REQUEST_CODE = 333;
    public static final int RASHOD_RECEIVED_REQUEST_CODE = 222;
    public static final int EDIT_RASHOD_RECEIVED_REQUEST_CODE = 223;
    public static final String EDIT_RASHOD_RECEIVED_MESSAGE = "editFinanceReceivedKey";

    RecyclerViewModelRashod recyclerViewModel;
    RashodAdapter adapter;

    //View components
    RecyclerView recyclerView;
    View view;


    public ListRashodFragment(){
        super(R.layout.fragment_rashodi);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewModel = new ViewModelProvider(getActivity()).get(RecyclerViewModelRashod.class);
        this.view = view;
        init(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        initObservers(view);

    }

    private void init(View view){
        initView(view);
        initListeners(view);
        initRecycler(view);
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.listRashodi);
    }

    private void initListeners(View view){

    }

    private void initRecycler(View view) {
        adapter = new RashodAdapter(new RashodDiffer(), rashod -> {
            Intent intent = new Intent(getActivity(), FinanceActivity.class);
            intent.setAction(FinanceActivity.ACTION_RASHOD);
            intent.putExtra(FinanceActivity.RASHOD_KEY, rashod);
            startActivityForResult(intent, RECEIVED_REQUEST_CODE);
            return null;

        }, rashodDelete -> {
            recyclerViewModel.deleteRashod(rashodDelete.getId());
            return null;

        }, rashodEdit -> {
            Intent intent = new Intent(getActivity(), EditFinanceActivity.class);
            intent.setAction(EditFinanceActivity.ACTION_RASHOD);
            intent.putExtra(EditFinanceActivity.RASHOD_KEY, rashodEdit);
            startActivityForResult(intent, EDIT_RASHOD_RECEIVED_REQUEST_CODE);
            return null;

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void initObservers(View view) {
        recyclerViewModel.getList().observe(getViewLifecycleOwner(), rashodi -> {
            adapter.submitList(rashodi);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDIT_RASHOD_RECEIVED_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    Rashod rashod = (Rashod) data.getExtras().getSerializable(EDIT_RASHOD_RECEIVED_MESSAGE);

                    recyclerViewModel.editRashod(rashod);
                    adapter.notifyDataSetChanged();

                }
                break;
        }
    }

}
