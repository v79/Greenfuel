package liamjdavison.co.uk.greenfuel;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by Liam Davison on 09/08/2016.
 */
public class FRViewHolder extends RecyclerView.ViewHolder {

	protected TextView rowDate, rowCost, rowFuel, rowOdo;
	protected TableLayout tableLayout;
	protected CardView cardView;

	public FRViewHolder(View itemView) {
		super(itemView);

		rowDate = (TextView) itemView.findViewById(R.id.rowDate);
		rowCost = (TextView) itemView.findViewById(R.id.rowCost);
		rowFuel = (TextView) itemView.findViewById(R.id.rowLitres);
		rowOdo = (TextView) itemView.findViewById(R.id.rowOdo);
		tableLayout = (TableLayout) itemView.findViewById(R.id.tableRow);
		cardView = (CardView) itemView.findViewById(R.id.card);

	}
}
