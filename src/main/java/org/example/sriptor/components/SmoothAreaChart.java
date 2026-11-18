package org.example.sriptor.components;

import javafx.geometry.Point2D;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.css.PseudoClass;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SmoothAreaChart extends AreaChart<String, Number> {
    private static final PseudoClass LINE_HOVERED = PseudoClass.getPseudoClass("line-hovered");
    private final Path smoothFill = new Path();
    private final Path smoothLine = new Path();
    private final Path lineHoverTarget = new Path();
    private final Tooltip revenueTooltip = new Tooltip();
    private final DecimalFormat revenueFormat = new DecimalFormat("#,##0.00");

    public SmoothAreaChart() {
        super(new CategoryAxis(), new NumberAxis());
        smoothFill.getStyleClass().add("smooth-area-fill");
        smoothLine.getStyleClass().add("smooth-area-line");
        lineHoverTarget.getStyleClass().add("smooth-area-hover-target");
        smoothFill.setMouseTransparent(true);
        smoothLine.setMouseTransparent(true);
        lineHoverTarget.setPickOnBounds(false);
        lineHoverTarget.setOnMouseEntered(event -> smoothLine.pseudoClassStateChanged(LINE_HOVERED, true));
        lineHoverTarget.setOnMouseExited(event -> {
            smoothLine.pseudoClassStateChanged(LINE_HOVERED, false);
            revenueTooltip.hide();
        });
        lineHoverTarget.setOnMouseMoved(this::showNearestRevenue);
    }

    @Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
        drawSmoothedSeries();
        getPlotChildren().removeAll(smoothFill, lineHoverTarget, smoothLine);
        getPlotChildren().addAll(smoothFill, lineHoverTarget, smoothLine);
    }

    private void drawSmoothedSeries() {
        smoothFill.getElements().clear();
        smoothLine.getElements().clear();
        lineHoverTarget.getElements().clear();
        if (getData() == null || getData().isEmpty()) {
            return;
        }

        List<Point2D> points = new ArrayList<>();
        XYChart.Series<String, Number> series = getData().get(0);
        for (XYChart.Data<String, Number> item : series.getData()) {
            if (item.getXValue() == null || item.getYValue() == null) {
                continue;
            }
            double x = getXAxis().getDisplayPosition(item.getXValue());
            double y = getYAxis().getDisplayPosition(item.getYValue());
            if (Double.isFinite(x) && Double.isFinite(y)) {
                points.add(new Point2D(x, y));
            }
        }
        if (points.size() < 2) {
            return;
        }

        double baseline = getYAxis().getDisplayPosition(0.0);
        smoothLine.getElements().add(new MoveTo(points.get(0).getX(), points.get(0).getY()));
        lineHoverTarget.getElements().add(new MoveTo(points.get(0).getX(), points.get(0).getY()));
        smoothFill.getElements().add(new MoveTo(points.get(0).getX(), baseline));
        smoothFill.getElements().add(new LineTo(points.get(0).getX(), points.get(0).getY()));

        for (int i = 0; i < points.size() - 1; i++) {
            Point2D start = points.get(i);
            Point2D end = points.get(i + 1);
            double dx = end.getX() - start.getX();
            double lowY = Math.min(start.getY(), end.getY());
            double highY = Math.max(start.getY(), end.getY());
            double control1Y = clamp(start.getY() + tangent(points, i) * dx / 3, lowY, highY);
            double control2Y = clamp(end.getY() - tangent(points, i + 1) * dx / 3, lowY, highY);

            CubicCurveTo curve = new CubicCurveTo(
                    start.getX() + dx / 3, control1Y,
                    end.getX() - dx / 3, control2Y,
                    end.getX(), end.getY());
            smoothLine.getElements().add(curve);
            lineHoverTarget.getElements().add(new CubicCurveTo(
                    start.getX() + dx / 3, control1Y,
                    end.getX() - dx / 3, control2Y,
                    end.getX(), end.getY()));
            smoothFill.getElements().add(new CubicCurveTo(
                    start.getX() + dx / 3, control1Y,
                    end.getX() - dx / 3, control2Y,
                    end.getX(), end.getY()));
        }

        Point2D last = points.get(points.size() - 1);
        smoothFill.getElements().add(new LineTo(last.getX(), baseline));
        smoothFill.getElements().add(new ClosePath());
    }

    private void showNearestRevenue(MouseEvent event) {
        if (getData() == null || getData().isEmpty()) {
            return;
        }

        XYChart.Data<String, Number> nearest = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        double pointerX = event.getX();
        for (XYChart.Data<String, Number> item : getData().get(0).getData()) {
            if (item.getXValue() == null || item.getYValue() == null) {
                continue;
            }
            double pointX = getXAxis().getDisplayPosition(item.getXValue());
            double distance = Math.abs(pointerX - pointX);
            if (distance < nearestDistance) {
                nearest = item;
                nearestDistance = distance;
            }
        }

        if (nearest != null) {
            revenueTooltip.setText(nearest.getXValue() + "\nGHS " +
                    revenueFormat.format(nearest.getYValue().doubleValue()));
            revenueTooltip.show(lineHoverTarget, event.getScreenX() + 14, event.getScreenY() + 14);
        }
    }

    private double tangent(List<Point2D> points, int index) {
        if (index == 0) {
            return slope(points.get(0), points.get(1));
        }
        if (index == points.size() - 1) {
            return slope(points.get(index - 1), points.get(index));
        }

        double previousSlope = slope(points.get(index - 1), points.get(index));
        double nextSlope = slope(points.get(index), points.get(index + 1));
        if (previousSlope * nextSlope <= 0) {
            return 0;
        }
        return (previousSlope + nextSlope) / 2;
    }

    private double slope(Point2D start, Point2D end) {
        double dx = end.getX() - start.getX();
        return dx == 0 ? 0 : (end.getY() - start.getY()) / dx;
    }

    private double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(value, maximum));
    }
}
