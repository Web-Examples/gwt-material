/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2017 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package gwt.material.design.client.base.mixin;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.UIObject;
import gwt.material.design.client.base.HasReadOnly;
import gwt.material.design.client.base.helper.StyleHelper;
import gwt.material.design.client.constants.CssName;
import gwt.material.design.jquery.client.api.Functions;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class ReadOnlyMixin<T extends UIObject & HasReadOnly, H extends UIObject> extends AbstractMixin<T>
        implements HasReadOnly {

    private H target;
    private Functions.Func1<Boolean> callback;

    public ReadOnlyMixin(T uiObject, H target) {
        super(uiObject);
        this.target = target;
    }

    public ReadOnlyMixin(T uiObject, H target, Functions.Func1<Boolean> callback) {
        super(uiObject);
        this.target = target;
        this.callback = callback;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        uiObject.removeStyleName(CssName.READ_ONLY);
        if (readOnly) {
            if (uiObject instanceof HasEnabled) {
                ((HasEnabled) uiObject).setEnabled(false);
            }
            uiObject.addStyleName(CssName.READ_ONLY);
            target.getElement().setAttribute("readonly", "");
        } else {
            if (uiObject instanceof HasEnabled) {
                ((HasEnabled) uiObject).setEnabled(true);
            }
            target.getElement().removeAttribute("readonly");
            uiObject.removeStyleName(CssName.READ_ONLY);
        }

        if (callback != null) {
            callback.call(readOnly);
        }
    }

    @Override
    public boolean isReadOnly() {
        return StyleHelper.containsStyle(uiObject.getStyleName(), CssName.READ_ONLY);
    }

    @Override
    public void setToggleReadOnly(boolean value) {
        uiObject.removeStyleName(CssName.READ_ONLY_TOGGLE);
        if (value) {
            uiObject.addStyleName(CssName.READ_ONLY_TOGGLE);
            if (uiObject instanceof HasEnabled && !((HasEnabled) uiObject).isEnabled()) {
                ((HasEnabled) uiObject).setEnabled(true);
            }
            $(uiObject).off("mousedown").mousedown((e, param1) -> {
                setReadOnly(false);
                return true;
            });

            $(target).off("blur").blur((e, param1) -> {
                setReadOnly(true);
                return true;
            });
        } else {
            $(uiObject).off("mousedown");
            $(target).off("blur");
        }
    }

    @Override
    public boolean isToggleReadOnly() {
        return StyleHelper.containsStyle(uiObject.getStyleName(), CssName.READ_ONLY_TOGGLE);
    }
}