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
package org.primefaces.component.contextmenu;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.api.Widget;
import org.primefaces.component.menu.AbstractMenu;

import org.primefaces.component.tieredmenu.TieredMenuRenderer;
import org.primefaces.util.WidgetBuilder;

public class ContextMenuRenderer extends TieredMenuRenderer {

    @Override
    protected void encodeScript(FacesContext context, AbstractMenu abstractMenu) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
        ContextMenu menu = (ContextMenu) abstractMenu;
		String clientId = menu.getClientId(context);
		UIComponent target = findTarget(context, menu);
        
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("ContextMenu", menu.resolveWidgetVar(), clientId, true);
        
        if(target != null) {
            wb.attr("target", target.getClientId(context))
                .attr("type", target.getClass().getSimpleName());
            
            if(target instanceof Widget) {
                wb.attr("targetWidgetVar", ((Widget) target).resolveWidgetVar());
            }
        }
        
        wb.attr("nodeType", menu.getNodeType(), null)
            .attr("event", menu.getEvent(), null)
            .callback("beforeShow", "function()", menu.getBeforeShow());
		
        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
	}
	
    @Override
    protected void encodeMarkup(FacesContext context, AbstractMenu abstractMenu) throws IOException{
        ContextMenu menu = (ContextMenu) abstractMenu;
        String style = menu.getStyle();
        String styleClass = menu.getStyleClass();
        styleClass = styleClass == null ? ContextMenu.CONTAINER_CLASS : ContextMenu.CONTAINER_CLASS + " " + styleClass;
        
        encodeMenu(context, menu, style, styleClass, "menu");
	}

    protected UIComponent findTarget(FacesContext context, ContextMenu menu) {
		String _for = menu.getFor();

		if(_for != null) {
			UIComponent forComponent = menu.findComponent(_for);
			if(forComponent == null) {
				throw new FacesException("Cannot find component '" + _for + "' in view.");
            }
			 
            return forComponent;   
		}
        else {
            return null;
        }
	}
}