package liamjdavison.co.uk.greenfuel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import liamjdavison.co.uk.greenfuel.model.FuelRecord;

/**
 * Created by Liam Davison on 09/08/2016.
 */
public class FuelRecordRecyclerAdapter extends RecyclerView.Adapter<FRViewHolder> {

	private List<FuelRecord> records;
	private Context context = null;
	private Currency currency = Currency.getInstance(Locale.getDefault());
	protected final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
	protected final DecimalFormat decimalFormat = new DecimalFormat("0.00");
	private SharedPreferences preferences;

	public FuelRecordRecyclerAdapter(Context context, List<FuelRecord> records) {
		this.records = records;
		this.context = context;
	}

	@Override
	public FRViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fuel_row_layout,parent,false);
		preferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());

		return new FRViewHolder(view);
	}

	/**
	 * This binds the variables from a record to the ViewHolder
	 *
	 * @param holder   holder to bind to
	 * @param position record number
	 */
	@Override
	public void onBindViewHolder(FRViewHolder holder, int position) {
		final FuelRecord record = records.get(position);

		holder.rowDate.setText(simpleDateFormat.format(record.getDate().getTime()));
		holder.rowCost.setText(new StringBuilder().append(currency.getSymbol()).append(record.getCost()));
		final String fuelText = new StringBuilder().append(decimalFormat.format(record.getFuelVolume())).toString();
		holder.rowFuel.setText(fuelText);
		holder.rowOdo.setText(record.getOdometer() == -1 ? "(none)" : String.valueOf(record.getOdometer()));

		holder.cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				View dView = LayoutInflater.from(v.getContext()).inflate()
			}
		});
	}

	/**
	 * Reset the records in the adapter?
	 *
	 * @param records
	 */
	public void setRecords(List<FuelRecord> records) {
		this.records = records;
	}

	public void refresh(List<FuelRecord> records) {
		this.records = records;
		notifyDataSetChanged();
	}
	/**
	 * @return number of records, or zero
	 */
	@Override
	public int getItemCount() {
		return (null != records ? records.size() : 0);
	}
}
