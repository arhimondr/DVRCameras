package drv.lombard.standart;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: andy
 * Date: 11/17/13
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TextValidator implements TextWatcher {
  private TextView textView;

  public TextValidator(TextView textView) {
    this.textView = textView;
  }

  public abstract void validate(TextView textView, String text);

  @Override
  final public void afterTextChanged(Editable s) {
    String text = textView.getText().toString();
    validate(textView, text);
  }

  @Override
  final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

  @Override
  final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
}
