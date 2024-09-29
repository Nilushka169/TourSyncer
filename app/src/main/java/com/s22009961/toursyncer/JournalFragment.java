package com.s22009961.toursyncer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JournalFragment extends Fragment {

    private EditText editTextEntry;
    private Button buttonSave;
    private RecyclerView recyclerViewEntries;
    private EntriesAdapter entriesAdapter;
    private List<String> journalEntries;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        editTextEntry = view.findViewById(R.id.editTextEntry);
        buttonSave = view.findViewById(R.id.buttonSave);
        recyclerViewEntries = view.findViewById(R.id.recyclerViewEntries);

        databaseHelper = new DatabaseHelper(getContext());

        // Get the email from the arguments
        if (getArguments() != null) {
            userEmail = getArguments().getString("email");
        }

        journalEntries = new ArrayList<>();
        entriesAdapter = new EntriesAdapter(journalEntries, new EntriesAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(int position) {
                showDeleteConfirmationDialog(position);
            }
        });

        recyclerViewEntries.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewEntries.setAdapter(entriesAdapter);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveJournalEntry();
            }
        });

        loadJournalEntries();

        return view;
    }

    private void saveJournalEntry() {
        String entry = editTextEntry.getText().toString();
        if (!entry.isEmpty()) {
            boolean isInserted = databaseHelper.insertJournalEntry(entry, userEmail);
            if (isInserted) {
                journalEntries.add(entry);
                entriesAdapter.notifyItemInserted(journalEntries.size() - 1);
                editTextEntry.setText("");  // Clear the EditText
                Toast.makeText(getActivity(), "Entry Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Error saving entry", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please write something before saving.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadJournalEntries() {
        Cursor cursor = databaseHelper.getJournalEntries(userEmail);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String entry = cursor.getString(cursor.getColumnIndex("entry"));
                journalEntries.add(entry);
            } while (cursor.moveToNext());
            cursor.close();
            entriesAdapter.notifyDataSetChanged();
        }
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this entry?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteJournalEntry(position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteJournalEntry(int position) {
        String entryToDelete = journalEntries.get(position);
        boolean isDeleted = databaseHelper.deleteJournalEntry(entryToDelete, userEmail);
        if (isDeleted) {
            journalEntries.remove(position);
            entriesAdapter.notifyItemRemoved(position);
            Toast.makeText(getActivity(), "Entry Deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Error deleting entry", Toast.LENGTH_SHORT).show();
        }
    }

    private static class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.EntryViewHolder> {

        private final List<String> entries;
        private final OnEntryClickListener onEntryClickListener;

        EntriesAdapter(List<String> entries, OnEntryClickListener onEntryClickListener) {
            this.entries = entries;
            this.onEntryClickListener = onEntryClickListener;
        }

        @NonNull
        @Override
        public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new EntryViewHolder(view, onEntryClickListener);
        }

        @Override
        public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
            String entry = entries.get(position);
            holder.bind(entry);
        }

        @Override
        public int getItemCount() {
            return entries.size();
        }

        static class EntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private final TextView textView;
            private final OnEntryClickListener onEntryClickListener;

            EntryViewHolder(View itemView, OnEntryClickListener onEntryClickListener) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
                this.onEntryClickListener = onEntryClickListener;
                itemView.setOnClickListener(this);
            }

            void bind(String entry) {
                textView.setText(entry);
            }

            @Override
            public void onClick(View v) {
                onEntryClickListener.onEntryClick(getAdapterPosition());
            }
        }

        interface OnEntryClickListener {
            void onEntryClick(int position);
        }
    }
}
