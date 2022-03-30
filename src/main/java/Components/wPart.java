package Components;

import java.util.ArrayList;

public class wPart {
    String id;
    ArrayList<wMeasure> wMeasureList = new ArrayList<>();

    public wPart(String name)
    {
        this.id = name;
    }

    public ArrayList<wMeasure> getMeasures()
    {
        return this.wMeasureList;
    }

    public void setMeasures(ArrayList<wMeasure> wMeasureList)
    {
        this.wMeasureList = wMeasureList;
    }

    public String toString()
    {
        String heading = "Part: " + this.id + "\n \n";
        String content = "";

        for(int i = 0; i < wMeasureList.size(); i++)
        {
            wMeasure currentWMeasure = wMeasureList.get(i);
            String measureHeading = "Measure " + (i + 1) + ": \n";
            String measureContent = currentWMeasure.toString() + "\n";
            String measureEnding = "--------------------- \n";
            content += measureHeading + measureContent + measureEnding;
        }

        String partEnding = "\n ##################### \n";

        return heading + content + partEnding;
    }

}