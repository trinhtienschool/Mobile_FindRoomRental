package com.trinhtien2212.findhomerental.ui.home;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mmin18.widget.RealtimeBlurView;
import com.trinhtien2212.findhomerental.PaginationScrollListener;
import com.trinhtien2212.findhomerental.R;
import com.trinhtien2212.findhomerental.adapter.RoomAdapter;
import com.trinhtien2212.findhomerental.model.Room;
import com.trinhtien2212.findhomerental.presenter.RoomsResult;
import com.trinhtien2212.findhomerental.presenter.SearchPresenter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements RoomsResult, PopupMenu.OnMenuItemClickListener, androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener {
    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private List<Room> mListlist;
    private ProgressBar progressBar;
    private SearchView searchView;
    private ImageButton btnFilter, btnSort;
    private TextView txtTotalResults;
    private RealtimeBlurView realtimeBlurView;
    private ProgressBar pb_waiting;
    private boolean isLoading, isLastPage;
    private int currentPage=1, totalPage=2;
    SearchPresenter searchPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recycler_view_room);
        progressBar = findViewById(R.id.progress_bar);
        btnFilter = findViewById(R.id.ImgButtonFilter);
        btnSort = findViewById(R.id.ImgButtonSort);
        txtTotalResults = findViewById(R.id.TextViewTotalResult);
        roomAdapter = new RoomAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mListlist = new ArrayList<Room>();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(roomAdapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                progressBar.setVisibility(View.VISIBLE);
                currentPage += 1;
                getListRoom();
//                loadNextPage();

            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
        searchPresenter = new SearchPresenter(this);
        Bundle bundle = getIntent().getExtras();
        String address = bundle.getString("address");
        search(address);
        realtimeBlurView = findViewById(R.id.realtimeBlurView);
        pb_waiting = findViewById(R.id.pb_waiting);

    }

//    private void loadNextPage() {
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //ToDo
//                getListRoom();
//                mListlist.addAll(list);
//                roomAdapter.notifyDataSetChanged();
//                isLoading = false;
//                progressBar.setVisibility(View.GONE);
//                if(currentPage == totalPage){
//                    isLastPage = true;
//                }
//            }
//        }, 2000);
//    }

    //Load data
    private void search(String address){
        searchPresenter.searchLocation(address);
//        showWaiting(View.VISIBLE);
    }
    private void getListRoom(){
       searchPresenter.getNext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                roomAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                roomAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    public void filterDistance(View v){
        androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(this,v);
        popup.setOnMenuItemClickListener(SearchActivity.this);
        popup.inflate(R.menu.distance_menu);
        popup.show();
    }
    public void sortPrice(View v){
        androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.price_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_distance_decrease:
                mListlist.clear();
                searchPresenter.sortDecrease();
                showWaiting(View.VISIBLE);
                Toast.makeText(this, "Sắp xếp theo khoảng cách giảm dần", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_distance_increase:
                mListlist.clear();
                searchPresenter.sortIncrease();
                showWaiting(View.VISIBLE);
                Toast.makeText(this, "Sắp xếp theo khoảng cách tăng dần", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    private void showWaiting(int waiting){
        realtimeBlurView.setVisibility(waiting);
        pb_waiting.setVisibility(waiting);
    }

    @Override
    public void returnRooms(List<Room> rooms) {
        if(rooms!=null){
            mListlist.addAll(rooms);
            roomAdapter.setData(mListlist);
            totalPage = searchPresenter.getTotalPage();
            txtTotalResults.setText(searchPresenter.getTotalResults()+"");
            if(currentPage == totalPage){
                isLastPage = true;
            }
            recyclerView.post(new Runnable() {
                public void run() {
                    // There is no need to use notifyDataSetChanged()
                    roomAdapter.notifyDataSetChanged();
                }
            });
            isLoading = false;
        }else Toast.makeText(this,"Không có kết quả",Toast.LENGTH_LONG).show();

        progressBar.setVisibility(View.GONE);
        showWaiting(View.INVISIBLE);
    }
}