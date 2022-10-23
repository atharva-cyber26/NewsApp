package com.example.acnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategorClickInterface {

    //e786b7928afb45c5bd0def4e9cfe074a

    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV =findViewById(R.id.idRVNews);
        categoryRV= findViewById(R.id.idRVCategories);
        loadingPB=findViewById(R.id.idPBLoading);
        articlesArrayList =new ArrayList<>();
        categoryRVModalArrayList =new ArrayList<>();
        newsRVAdapter =new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter =new CategoryRVAdapter(categoryRVModalArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("ALL");
        newsRVAdapter.notifyDataSetChanged();
    }
     private  void getCategories()
    {
        categoryRVModalArrayList.add(new CategoryRVModal("All","https://images.unsplash.com/photo-1586339949916-3e9457bef6d3?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal("TECHNOLOGY" ,"https://images.unsplash.com/photo-1485827404703-89b55fcc595e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80 "));
        categoryRVModalArrayList.add(new CategoryRVModal("SCIENCE","https://images.unsplash.com/photo-1554475900-0a0350e3fc7b?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=359&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal("SPORTS","https://images.unsplash.com/photo-1517649763962-0c623066013b?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal("GENERAL","https://images.unsplash.com/photo-1432821596592-e2c18b78144f?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal("BUSINESS","https://images.unsplash.com/photo-1444653614773-995cb1ef9efa?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1055&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal("ENTERTAINMENT","https://images.unsplash.com/photo-1593437783747-66ed2e10a9af?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1049&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal("HEALTH","https://images.unsplash.com/photo-1447452001602-7090c7ab2db3?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1050&q=80"));
        categoryRVAdapter.notifyDataSetChanged();
    }

    private void getNews(String category)
    {
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL ="https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apiKey=e786b7928afb45c5bd0def4e9cfe074a";
        String url="https://newsapi.org/v2/top-headlines?country=in&apiKey=e786b7928afb45c5bd0def4e9cfe074a";
        String BASE_URL ="https://newsapi.org/";
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI=retrofit.create(RetrofitAPI.class);
        Call<NewsModal>call;
        if(category.equals("ALL"))
        {
            call=retrofitAPI.getAllNews(url);
        }
        else
        {
            call=retrofitAPI.getNewsByCategory(categoryURL);
        }
       call.enqueue(new Callback<NewsModal>() {
           @Override
           public void onResponse(Call<NewsModal> call, Response<NewsModal> response)
           {
               NewsModal newsModal=response.body();
               loadingPB.setVisibility(View.GONE);
               ArrayList<Articles> articles=newsModal.getArticles();
               for(int i=0;i<articles.size();i++)
               {
                   articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),
                           articles.get(i).getUrl(),articles.get(i).getContent()));

               }
               newsRVAdapter.notifyDataSetChanged();


           }

           @Override
           public void onFailure(Call<NewsModal> call, Throwable t)
           {
               Toast.makeText(MainActivity.this,"Fail to get news",Toast.LENGTH_SHORT).show();

           }
       });



    }

    @Override
    public void onCategoryClick(int position)
    {
        String category =categoryRVModalArrayList.get(position).getCategory();
        getNews(category);


    }
}