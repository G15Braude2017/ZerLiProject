package QuarterReports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;

public class Complaint_Report_Controller {
    @FXML
    private StackedBarChart ComplaintChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    public void initialize()
    {
    	XYChart.Series set=new XYChart.Series<>();
    	set.getData().add(new XYChart.Data("month one",150));
    }
    @FXML
    void click_StoreManagerReports_backBtn(ActionEvent event) {

    }
}
