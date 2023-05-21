package com.example.tasksapp.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasksapp.models.Appointment;
import com.example.tasksapp.R;

import java.util.List;


public class MyAppointmentsAdapter extends RecyclerView.Adapter<MyAppointmentsAdapter.VH> {

    List<Appointment> appointments;
    Context context;

    public MyAppointmentsAdapter(List<Appointment> appointments, Context context) {
        this.appointments = appointments;
        this.context = context;
    }

    public void clearList(){
        appointments.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyAppointmentsAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);
        return new VH(v);
    }

    String priority = "";

    @Override
    public void onBindViewHolder(@NonNull MyAppointmentsAdapter.VH holder, int position) {
        holder.name.setText(appointments.get(position).getName());
        holder.type.setText(appointments.get(position).getType());
        holder.date.setText(appointments.get(position).getDate());
        holder.time.setText(appointments.get(position).getTime());
        holder.notes.setText(appointments.get(position).getNotes());

        Appointment appointment = appointments.get(position);
        
        if (appointment.getImportance() == 1) {
            priority = "high";
            holder.priority.setText(priority);
            holder.appointmentLayout.setCardBackgroundColor(context.getColor(R.color.red));
        } else if (appointment.getImportance() == 2) {
            priority = "medium";
            holder.priority.setText(priority);
            holder.appointmentLayout.setCardBackgroundColor(context.getColor(R.color.orange));
        } else if (appointment.getImportance() == 3){
            priority = "low";
            holder.priority.setText(priority);
            holder.appointmentLayout.setCardBackgroundColor(context.getColor(R.color.yellow));
        }

        Log.e("Adapter", "Priority >> " + appointment.getImportance());

        holder.appointmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AppointmentDetailsActivity.class);
                intent.putExtra("name", appointment.getName());
                intent.putExtra("type", appointment.getType());
                intent.putExtra("date", appointment.getDate());
                intent.putExtra("time", appointment.getTime());
                intent.putExtra("notes", appointment.getNotes());
                intent.putExtra("priority", priority);
                intent.putExtra("index", position);
                context.startActivity(intent);
            }
        });

        Log.e("Adapter", "onBindViewHolder: " + appointment.getImportance());
        Log.e("Adapter", "count: " + appointments.size());
    }

    @Override
    public int getItemCount() {
        if (appointments != null)
            return appointments.size();
        else return 0;
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView name, type, date, time, notes, priority;
        CardView appointmentLayout;

        public VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            notes = itemView.findViewById(R.id.notes);
            priority = itemView.findViewById(R.id.priority);
            appointmentLayout = itemView.findViewById(R.id.cvAppointment);

        }
    }
}
