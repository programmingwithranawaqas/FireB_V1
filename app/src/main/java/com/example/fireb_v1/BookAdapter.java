package com.example.fireb_v1;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BookAdapter extends FirebaseRecyclerAdapter<Book, BookAdapter.BookViewHolder> {

    Context context;
    public BookAdapter(Context context, @NonNull FirebaseRecyclerOptions<Book> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookViewHolder holder, int i, @NonNull Book book) {
        holder.tvPrice.setText(book.getPrice()+"");
        holder.tvName.setText(book.getName());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvISSN.setText(book.getIssn()+"");

        String key = getRef(i).getKey();

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v1) {
                AlertDialog.Builder add = new AlertDialog.Builder(context);
                add.setTitle("Update Book");
                View v = LayoutInflater.from(context)
                        .inflate(R.layout.add_book_form, null, false);
                add.setView(v);

                // hooks of form
                TextInputEditText etName, etPrice, etIssnNumber, etBookAuthor;
                etName = v.findViewById(R.id.etBookName);
                etPrice = v.findViewById(R.id.etBookPrice);
                etIssnNumber = v.findViewById(R.id.etIssnNumber);
                etBookAuthor = v.findViewById(R.id.etBookAuthor);
                //
                etName.setText(book.getName());
                etIssnNumber.setText(book.getIssn()+"");
                etBookAuthor.setText(book.getAuthor());
                etPrice.setText(book.getPrice()+"");

                add.setPositiveButton("Update Book", new DialogInterface.OnClickListener() {
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

                        assert key != null;
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Books")
                                .child(key)
                                .setValue(record)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Book updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                add.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Books")
                                .child(key)
                                .removeValue()
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Book deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });


                add.show();
                return false;
            }
        });
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_book_design, parent, false);
        return new BookViewHolder(v);
    }

    public class BookViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName, tvAuthor, tvPrice, tvISSN;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvName = itemView.findViewById(R.id.tvName);
            tvISSN = itemView.findViewById(R.id.tvISSN);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
