package com.koreait.picsum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvList;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new MyAdapter();
        rvList = findViewById(R.id.rvList);
        rvList.setAdapter(adapter);

        getList();
    }
    private void getList(){ //생성자를 이용하는것인데 원하는값만 이용할때 사용
        Retrofit ret = new Retrofit.Builder()
                .baseUrl("https://picsum.photos/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PicsumService service = ret.create(PicsumService.class);
        Call<List<PicsumVO>> call = service.getPicsumList();
        call.enqueue(new Callback<List<PicsumVO>>() {
            @Override
            public void onResponse(Call<List<PicsumVO>> call, Response<List<PicsumVO>> response) {
                if(response.isSuccessful()){
                    List<PicsumVO> list = response.body();
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<PicsumVO>> call, Throwable t) {

            }
        });

    }
}
// 상속 받았을 때 에러가 발생할 수 있는 이유 2가지
// 1. 추상 메소드를 가지고 있는 클래스를 상속(이 상황에서는 1번)
// 추상 메소드(abstract) : 구현부가 없는 메소드
// 2. 부모 생성자 문제 (부모가 기본 생성자 안 가지고 있으면)
class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<PicsumVO> list;//주소값을 외부에서 받겠다, 기존에 데이터는 날아감
    //새로운 버스를 받고 기존 버스는 보낸다.

//    private List<PicsumVO> list2 = new ArrayList();//내부의 item 들만 바꿔서 사용
//    //외부에서 내부 승객정보 받아서 뿌린다, 추가로 더 받는다고해서 데이터가 날아가지 않고
//    //계속해서 쌓인다
//    //기존의 버스에 계속해서 추가한다. ** 페이징 **

    public void setList(List<PicsumVO> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//layout 객체화 담당
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picsum, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PicsumVO data = list.get(position);
        holder.setItem(data);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();

//        if (list == null) {
//            return 0;
//        }
//        return list.size();
    }
    //생성자 문제
    static class MyViewHolder extends RecyclerView.ViewHolder {
        //연결
        private ImageView ivImg;
        private TextView tvAuthor;

        public MyViewHolder(@NonNull View v) {
            super(v);
            ivImg = v.findViewById(R.id.ivImg);
            tvAuthor = v.findViewById(R.id.tvAuthor);
        }

        public void setItem(PicsumVO param) {
            tvAuthor.setText(param.getAuthor());
            Glide.with(ivImg).load(param.getDownload_url()).into(ivImg);
            //4-24:30 참고

        }
    }
}