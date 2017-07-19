package net.flow9.rxandroidbasic04;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    List<String> data = new ArrayList<>();
    RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        adapter = new RecyclerAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        emitData();
    }
    Observable<String> observable;
    public void emitData(){
        // 1월~ 12월까지 텍스트를 추출
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        // 초당 1개씩 데이터 발행
        observable = Observable.create(emitter -> {
            for(String month : months){
                emitter.onNext(month);
                Thread.sleep(1000);
            }
            emitter.onComplete();
        });
    }
    public void doMap(View view) {

    }

    public void doFlatmap(View view) {

    }

    public void doZip(View view) {

    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }
}

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {
    LayoutInflater inflater = null;
    List<String> data = null;

    public RecyclerAdapter(Context context, List<String> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Log.i("Refresh", "~~~~~~~~~~~~~~~~~position=" + position);
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView textView;

        public Holder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}