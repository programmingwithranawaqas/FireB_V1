package com.example.fireb_v1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabAdd;
    DatabaseReference reference;
    RecyclerView rvBooks;
    BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBook();
            }
        });


    }
    private void addNewBook()
    {
        AlertDialog.Builder add = new AlertDialog.Builder(this);
        add.setTitle("New Book");
        View v = LayoutInflater.from(this)
                        .inflate(R.layout.add_book_form, null, false);
        add.setView(v);

        // hooks of form
        TextInputEditText etName, etPrice, etIssnNumber, etBookAuthor;
        etName = v.findViewById(R.id.etBookName);
        etPrice = v.findViewById(R.id.etBookPrice);
        etIssnNumber = v.findViewById(R.id.etIssnNumber);
        etBookAuthor = v.findViewById(R.id.etBookAuthor);
        //

        add.setPositiveButton("Add Book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString().trim();
                Double price = Double.parseDouble(etPrice.getText().toString());
                int issn = Integer.parseInt(etIssnNumber.getText().toString());
                String bookAuthor = etBookAuthor.getText().toString().trim();

                HashMap<String, Object> record = new HashMap<>();
                record.put("name", name);
                record.put("issn", issn);
                record.put("author", bookAuthor);
                record.put("price", price);

                reference.child("Books")
                        .push()
                        .setValue(record)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Book saved", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        add.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        add.show();
    }

    private void init()
    {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fabAdd = findViewById(R.id.fabAdd);
        rvBooks = findViewById(R.id.rvBooks);
        rvBooks.setHasFixedSize(true);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(reference.child("Books"), Book.class)
                        .build();

        adapter = new BookAdapter(this, options);
        rvBooks.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}