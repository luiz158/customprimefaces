/*
 * Copyright 2009-2012 Prime Teknoloji.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.component.overlaypanel;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class OverlayPanelRenderer extends CoreRenderer {
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        OverlayPanel panel = (OverlayPanel) component;
        
        if(panel.isContentLoadRequest(context)) {
            renderChildren(context, panel);
        }
        else {
            encodeMarkup(context, panel);
            encodeScript(context, panel);
        }
    }

    protected void encodeMarkup(FacesContext context, OverlayPanel panel) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = panel.getClientId(context);
        String style = panel.getStyle();
        String styleClass = panel.getStyleClass();
        styleClass = styleClass == null ? OverlayPanel.STYLE_CLASS : OverlayPanel.STYLE_CLASS + " " + styleClass;
        
        writer.startElement("div", panel);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if(style != null) {
            writer.writeAttribute("style", style, "style");
        }
        
        if(!panel.isDynamic()) {
            renderChildren(context, panel);
        }
        
        writer.endElement("div");
    }
    
    protected void encodeScript(FacesContext context, OverlayPanel panel) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        UIComponent target = panel.findComponent(panel.getFor());
        if(target == null) {
            throw new FacesException("Cannot find component '" + panel.getFor() + "' in view.");
        }
        
        String clientId = panel.getClientId(context);
        String targetClientId = target.getClientId(context);
        
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("OverlayPanel", panel.resolveWidgetVar(), clientId, true)
            .attr("target", targetClientId)
            .attr("showEvent", panel.getShowEvent(), null)
            .attr("hideEvent", panel.getHideEvent(), null)
            .attr("showEffect", panel.getShowEffect(), null)
            .attr("hideEffect", panel.getHideEffect(), null)
            .callback("onShow", "function()", panel.getOnShow())
            .callback("onHide", "function()", panel.getOnHide())
            .attr("my", panel.getMy(), null)
            .attr("at", panel.getAt(), null)
            .attr("appendToBody", panel.isAppendToBody(), false)
            .attr("dynamic", panel.isDynamic(), false);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }
    
    @Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		//Do nothing
	}

    @Override
	public boolean getRendersChildren() {
		return true;
	}
}