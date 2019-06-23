package com.example.robinkopecky.pizza;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BagActivity extends AppCompatActivity {

    private long priceTotal = 0;


    private RecyclerView recyclerView;
    private TextView total;
    private PizzaDAOImplementation pizzaDAOImplementation;
    private List<Pizza> bagList = new ArrayList<>();

    private PizzaAdapter pizzaAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FloatingActionButton trash = (FloatingActionButton) findViewById(R.id.trash);
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pizzaDAOImplementation.emptyBag();
                finish();
                startActivity(getIntent());
            }
        });
        Button sendOrder = (Button) findViewById(R.id.sent);
        sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pizzaDAOImplementation.emptyBag();
                Toast toast = Toast.makeText(BagActivity.this,getResources().getString(R.string.ordering)+": "+ priceTotal+" Kč",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
                finish();
                startActivity(MainActivity.getIntent(BagActivity.this, null));
            }
        });
        total = findViewById(R.id.totalPrice);
        recyclerView = findViewById(R.id.recycler_view);
        pizzaDAOImplementation = new PizzaDAOImplementation(this);

        total.setText("0 Kč");

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



    public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder>{

        @NonNull
        @Override
        public PizzaAdapter.PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(BagActivity.this).inflate(R.layout.row_bag_list, viewGroup,false);
            return new PizzaAdapter.PizzaViewHolder(itemView);
        }



        @Override
        public void onBindViewHolder(@NonNull final PizzaAdapter.PizzaViewHolder pizzaViewHolder, final int i) {

            final Pizza pizza = bagList.get(i);

            final long price = Long.parseLong(pizza.getPrice());
            priceTotal += price;
            total.setText(priceTotal + " Kč");
            pizzaViewHolder.pizzaName.setText(pizza.getName());
            pizzaViewHolder.pizzaDescription.setText(pizza.getDescription());
            pizzaViewHolder.pizzaPrice.setText(pizza.getPrice() + " Kč");


            pizzaViewHolder.deletePizza.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long pom = Long.parseLong(pizza.getPrice());
                    priceTotal = priceTotal - pom;
                    total.setText(priceTotal + " Kč");
                    pizzaDAOImplementation.deleteBag(pizza);

                    finish();
                    startActivity(getIntent());
                }
            });


        }

        @Override
        public int getItemCount() {
            return bagList.size();
        }

        public class PizzaViewHolder extends RecyclerView.ViewHolder{

            public TextView pizzaName;
            public TextView pizzaDescription;
            public TextView pizzaPrice;
            public Button deletePizza;

            public PizzaViewHolder(@NonNull View itemView) {
                super(itemView);
                pizzaName = itemView.findViewById(R.id.pizza_name);
                pizzaDescription = itemView.findViewById(R.id.pizza_description);
                pizzaPrice = itemView.findViewById(R.id.pizza_price);
                deletePizza = itemView.findViewById(R.id.remove);

            }
        }
    }

    private void setList(){
        bagList.clear();
        bagList.addAll(pizzaDAOImplementation.getPizzaBag());

        if (pizzaAdapter == null){

            pizzaAdapter = new PizzaAdapter();
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(pizzaAdapter);

        } else {
            pizzaAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        total.setText(priceTotal + " Kč");
        setList();
    }

}
