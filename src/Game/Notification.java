package Game;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class Notification {
    private Label textLabel;

    public Notification(Label textLabel)
    {
        this.textLabel = textLabel;
    }

    public void setText(String s)
    {
        textLabel.setText(s);
    }

    public void setColor(Color c)
    {
        textLabel.setTextFill(c);
    }
}
