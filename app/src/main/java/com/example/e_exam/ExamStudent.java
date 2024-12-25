package com.example.e_exam;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExamStudent extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExamAdapter examAdapter;
    private List<Exam> examList;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_exam_student); // Đảm bảo tên file layout đúng

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        examList = new ArrayList<>();
        examAdapter = new ExamAdapter(examList);
        recyclerView.setAdapter(examAdapter);

        // Nhận giá trị className từ Intent
        className = getIntent().getStringExtra("CLASS_NAME");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("exams");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                examList.clear();
                for (DataSnapshot examSnapshot : dataSnapshot.getChildren()) {
                    String examClassName = examSnapshot.child("class").getValue(String.class);
                    String name = examSnapshot.child("name").getValue(String.class);
                    if (examClassName != null && examClassName.equals(className) && name != null) {
                        examList.add(new Exam(name));
                    }
                }
                examAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ExamStudent", "Failed to read exams", databaseError.toException());
            }
        });
    }

    private static class Exam {
        String name;

        Exam(String name) {
            this.name = name;
        }
    }

    private static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView examName;

        ExamViewHolder(View itemView) {
            super(itemView);
            examName = itemView.findViewById(R.id.tvExamName);
        }
    }

    private class ExamAdapter extends RecyclerView.Adapter<ExamViewHolder> {
        private List<Exam> examList;

        ExamAdapter(List<Exam> examList) {
            this.examList = examList;
        }

        @NonNull
        @Override
        public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
            return new ExamViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
            Exam exam = examList.get(position);
            holder.examName.setText(exam.name);
        }

        @Override
        public int getItemCount() {
            return examList.size();
        }
    }
}
