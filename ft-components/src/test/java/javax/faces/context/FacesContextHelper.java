package javax.faces.context;

public class FacesContextHelper {

    public static void setFacesContext(FacesContext context) {
        FacesContext.setCurrentInstance(context);
    }
}
