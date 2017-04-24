// Generated code from Butter Knife. Do not modify!
package com.lilyondroid.lily.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ActivityLogin$$ViewBinder<T extends com.lilyondroid.lily.activities.ActivityLogin> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755215, "field '_emailText'");
    target._emailText = finder.castView(view, 2131755215, "field '_emailText'");
    view = finder.findRequiredView(source, 2131755216, "field '_passwordText'");
    target._passwordText = finder.castView(view, 2131755216, "field '_passwordText'");
    view = finder.findRequiredView(source, 2131755218, "field '_loginButton'");
    target._loginButton = finder.castView(view, 2131755218, "field '_loginButton'");
    view = finder.findRequiredView(source, 2131755219, "field '_signupLink'");
    target._signupLink = finder.castView(view, 2131755219, "field '_signupLink'");
    view = finder.findRequiredView(source, 2131755217, "field '_cancelButton'");
    target._cancelButton = finder.castView(view, 2131755217, "field '_cancelButton'");
  }

  @Override public void unbind(T target) {
    target._emailText = null;
    target._passwordText = null;
    target._loginButton = null;
    target._signupLink = null;
    target._cancelButton = null;
  }
}
