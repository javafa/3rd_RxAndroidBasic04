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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    Observable<Integer> observable;
    Observable<String> observableZip;
    String[] months;
    public void emitData(){
        // 1월~ 12월까지 텍스트를 추출
        DateFormatSymbols dfs = new DateFormatSymbols();
        months = dfs.getMonths();
        // 초당 1개씩 데이터 발행
        observable = Observable.create(emitter -> {
            for(int i=0 ; i<12 ; i++){
                emitter.onNext(i);
                Thread.sleep(1000);
            }
            emitter.onComplete();
        });
        // 초당 한번씩 출력하는 로직으로 변경 고민...
        observableZip = Observable.zip(
                Observable.just("Donho Go","BeWhy"),
                Observable.just("Programmer","Rapper"),
                (item1, item2) -> "name:"+item1+", job:"+item2
        );
    }
    public void doMap(View view) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter( item -> item.equals("May")?false:true)
                .map( item -> ">> "+item+"")
                .subscribe(
                item -> {
                    data.add(item);
                    adapter.notifyItemInserted(data.size()-1);
                },
                error -> Log.e("Error",error.getMessage()),
                () -> Log.i("Complete","Successfully completed!")
        );
    }

    public void doFlatmap(View view) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(item -> Observable.fromArray(new String[]{"name:"+months[item], "code:"+item}))
                .subscribe(
                        item -> {
                            data.add(item);
                            adapter.notifyItemInserted(data.size()-1);
                        },
                        error -> Log.e("Error",error.getMessage()),
                        () -> Log.i("Complete","Successfully completed!")
                );
    }

    public void doZip(View view) {
        observableZip
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    item -> {
                        data.add(item+"");
                        adapter.notifyItemInserted(data.size()-1);
                    },
                    error -> Log.e("Error",error.getMessage()),
                    () -> Log.i("Complete","Successfully completed!")
                );
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