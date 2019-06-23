package com.example.robinkopecky.pizza;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class BagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Actionbag", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public static Intent getIntent(MainActivity context, Long id) {
        Intent intent = new Intent(context,
                BagActivity.class);
        if (id != null){
            intent.putExtra(IntentConstants.ID, id);
        }
        return intent;
    }
    public static Intent getIntent(FavoriteActivity context, Long id) {
        Intent intent = new Intent(context,
                BagActivity.class);
        if (id != null){
            intent.putExtra(IntentConstants.ID, id);
        }
        return intent;
    }
    public static Intent getIntent(PizzaDetailActivity context, Long id) {
        Intent intent = new Intent(context,
                BagActivity.class);
        if (id != null){
            intent.putExtra(IntentConstants.ID, id);
        }
        return intent;
    }

}
