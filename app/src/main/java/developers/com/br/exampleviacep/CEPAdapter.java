package developers.com.br.exampleviacep;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by AndreCoelho on 14/10/2017.
 */

public class CEPAdapter extends RecyclerView.Adapter<CEPAdapter.ViewHolder> {

    interface callbackmap{
        void setPosition(String data);
    }

    private List<JSONObject> jsonObjectList;
    private Context context;
    private callbackmap callbackmap;

    public CEPAdapter(Context context, List<JSONObject> jsonObjectList, callbackmap callbackmap) {
        this.context = context;
        this.jsonObjectList = jsonObjectList;
        this.callbackmap = callbackmap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cep_item, parent, false);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(jsonObjectList.get(position));
    }

    @Override
    public int getItemCount() {
        return jsonObjectList.size();
    }

    public void addCep(JSONObject jsonObject){
        jsonObjectList.add(jsonObject);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtaddress;
        private TextView txtcep;
        private TextView txtCity;
        private JSONObject data;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtaddress = (TextView) itemView.findViewById(R.id.txtAddress);
            txtcep = (TextView) itemView.findViewById(R.id.txtcepnumber);
            txtCity = (TextView) itemView.findViewById(R.id.txtCity);
            cardView = (CardView) itemView.findViewById(R.id.categoria_cardview);
            cardView.setOnClickListener(this);

        }
        //pega os dados do Adapter e adiciona na viu
        public void bindData(JSONObject category){
            data = category;
            try {
                txtaddress.setText(data.getString("logradouro"));
                txtcep.setText(data.getString("cep"));
                txtCity.setText(data.getString("localidade"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Lê um click
        @Override
        public void onClick(View view) {
            /*Intent it = new Intent(context, Main2Activity.class);
            it.putExtra("CATEGORIA_TAG", data.toString());
            context.startActivity(it);*/

            if(callbackmap != null){
                callbackmap.setPosition(data.toString());
            }
        }
    }
}
