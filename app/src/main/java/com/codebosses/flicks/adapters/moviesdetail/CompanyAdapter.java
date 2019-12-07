package com.codebosses.flicks.adapters.moviesdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.moviespojo.moviedetail.ProductionCompany;
import com.codebosses.flicks.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyHolder> {

    private List<ProductionCompany> productionCompanyList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public CompanyAdapter(Context context, List<ProductionCompany> productionCompanyList) {
        this.context = context;
        this.productionCompanyList = productionCompanyList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CompanyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompanyHolder(layoutInflater.inflate(R.layout.row_conpany, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyHolder holder, int position) {
        ProductionCompany productionCompany = productionCompanyList.get(position);
        if (productionCompany != null) {
            Glide.with(context)
                    .load(EndpointUrl.POSTER_BASE_URL + productionCompany.getLogo_path())
                    .thumbnail(0.1f)
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                    .into(holder.imageViewLogo);
            holder.textViewName.setText(productionCompany.getName());
        }
    }

    @Override
    public int getItemCount() {
        return productionCompanyList.size();
    }

    class CompanyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewCompanyLogoCompanyRow)
        ImageView imageViewLogo;
        @BindView(R.id.textViewCompanyNameCompanyRow)
        TextView textViewName;

        public CompanyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            FontUtils.getFontUtils(context).setTextViewRegularFont(textViewName);
        }
    }
}
