package uk.co.slc.sfd.cp.ft.jsf;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uk.co.slc.sfd.cp.ft.jsf.messages.MessageProvider;

public abstract class MockFacesContext extends FacesContext {

    private static final Release RELEASE = new Release();

    private List<FacesMessage> messages = new ArrayList<FacesMessage>();

    private static class Release implements Answer<Void> {

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            setCurrentInstance(null);
            return null;
        }
    }

    public static FacesContext mockFacesContext() {
        FacesContext context = Mockito.mock(FacesContext.class);
        Application application = Mockito.mock(Application.class);
        ExternalContext external = Mockito.mock(ExternalContext.class);
        ResourceBundle bundle = Mockito.mock(ResourceBundle.class);
        RenderKit renderKit = Mockito.mock(RenderKit.class);
        UIViewRoot viewRoot = Mockito.mock(UIViewRoot.class);
        ELContext elContext = Mockito.mock(ELContext.class);
        when(context.getApplication()).thenReturn(application);
        when(context.getExternalContext()).thenReturn(external);
        when(application.getResourceBundle(context, MessageProvider.BUNDLE_KEY)).thenReturn(bundle);
        when(context.getRenderKit()).thenReturn(renderKit);
        when(context.getViewRoot()).thenReturn(viewRoot);
        when(context.getELContext()).thenReturn(elContext);
        when(renderKit.getRenderer(Matchers.anyString(), Matchers.anyString())).thenReturn(Mockito.mock(Renderer.class));

        setCurrentInstance(context);
        Mockito.doAnswer(RELEASE).when(context).release();
        return context;
    }

    @Override
    public void addMessage(String clientId, FacesMessage message) {
        messages.add(message);
    }

    @Override
    public Iterator<FacesMessage> getMessages() {
        return messages.iterator();
    }

    @Override
    public List<FacesMessage> getMessageList() {
        return messages;
    }

}
