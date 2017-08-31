package application;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Circle;
import javax.imageio.ImageIO;
import application.RootLayout;
import java.io.PrintStream;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import application.RootLayout;

public class DraggableNode extends AnchorPane {

    /**
     * @return the num
     */
    public Integer getNum() {
        return num;
    }

    /**
     * @param num the num to set
     */
    public void setNum(Integer num) {
        this.num = num;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @param altura the altura to set
     */
    public void setAltura(double altura) {
        this.altura = altura;
    }

    /**
     * @param largura the largura to set
     */
    public void setLargura(double largura) {
        this.largura = largura;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @return the altura
     */
    public double getAltura() {
        return 50;
    }

    /**
     * @return the largura
     */
    public double getLargura() {
        return 300;
    }

    @FXML
    private AnchorPane root_pane;
    @FXML
    private Label title_bar;
    @FXML
    private Label close_button;
    @FXML
    ImageView lixeira;

    private double x;
    private double y;
    private double altura;
    private double largura;

    DraggableNode target = null;

    Observer observer;

    Bounds objA;

    private String tipo;
    private String nome;
    private String valor;
    int num;

    private EventHandler<DragEvent> mContextDragOver;
    private EventHandler<DragEvent> mContextDragDropped;

    DragIconType mType = null;

    Point2D mDragOffset = new Point2D(0.0, 0.0);

    private final DraggableNode self;

    private AnchorPane right_pane, pane = null;

    private final List<String> mLinkIds = new ArrayList<String>();

    public DraggableNode(RootLayout rootPane) {
        observer = new Observer(this, rootPane);
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/DraggableNode.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        self = this;
        pane = rootPane;

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        //provide a universally unique identifier for this object
        setId(UUID.randomUUID().toString());

        self.setNum(rootPane.getFlag());
        //System.out.println(num);
        //num++;

        this.lixeira = rootPane.lixeira;

        objA = lixeira.localToScene(lixeira.getBoundsInLocal());

        self.setTipo("DEFINIR TIPO");
        self.setNome("DEFINIR NOME");
        self.setValor("DEFINIR VALOR");

    }

    @FXML
    private void initialize() {

        buildNodeDragHandlers();

        parentProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable,
                    Object oldValue, Object newValue) {
                right_pane = (AnchorPane) getParent();

            }

        });
    }

    public void registerLink(String linkId) {
        mLinkIds.add(linkId);
    }

    public void relocateToPoint(Point2D p) {

        //relocates the object to a point that has been converted to
        //scene coordinates
        Point2D localCoords = getParent().sceneToLocal(p);

        relocate(
                (int) (localCoords.getX() - mDragOffset.getX()),
                (int) (localCoords.getY() - mDragOffset.getY())
        );
    }

    public DragIconType getType() {
        return mType;
    }

    public void setType(DragIconType type) {

        mType = type;

        getStyleClass().clear();
        getStyleClass().add("dragicon");

        switch (mType) {

            case input:
                getStyleClass().remove("input");
                getStyleClass().add("input2");
                break;

            case loop:
                getStyleClass().remove("loop");
                getStyleClass().add("loop2");
                break;

            case var_const:
                getStyleClass().remove("var_const");
                getStyleClass().add("var_const2");
                break;

            case condition:
                getStyleClass().remove("condition");
                getStyleClass().add("condition2");
                break;
                
            case elsee:
                getStyleClass().remove("elsee");
                getStyleClass().add("elsee2");
                break;

            case output:
                getStyleClass().remove("output");
                getStyleClass().add("output2");
                break;

            case arithmetic:
                getStyleClass().remove("arithmetic");
                getStyleClass().add("arithmetic2");
                break;

            default:
                break;
        }
    }

    public void buildNodeDragHandlers() {

        mContextDragOver = new EventHandler<DragEvent>() {

            //dragover to handle node dragging in the right pane view
            @Override
            public void handle(DragEvent event) {

                event.acceptTransferModes(TransferMode.ANY);
                relocateToPoint(new Point2dSerial(event.getSceneX() - 150, event.getSceneY() - 25));

                //System.out.println(self.localToScene(self.getBoundsInLocal()));
                //System.out.println(lixeira.localToScene(lixeira.getBoundsInLocal()));
                Bounds objB = self.localToScene(self.getBoundsInLocal());
               
                for (Node n : right_pane.getChildren()) {
                    //target = (DraggableNode) n;
                    Bounds target;
                    target = n.localToScene(n.getBoundsInLocal());
                    if (objB.intersects(target) && getId() != n.getId()) {
                        n.setOpacity(0.3);
                    } else {
                        n.setOpacity(1);
                    }
                }

                if (objB.intersects(objA)) {
                    self.setOpacity(0.3);
                } else {
                    self.setOpacity(1);
                }

                event.consume();
/*
                for (Node n : right_pane.getChildren()) {
                    target = (DraggableNode) n;
                    if (getId() != target.getId()) {
                        if (target.contem(self.getLayoutX(), self.getLayoutY(), self.getLayoutX() + self.getLargura(), self.getLayoutY() + self.getAltura())) {
                            System.out.println("COLISÃO DETECTADA ENTRE " + self.mType + " e " + target.mType);
                        } else {
                            System.out.println();
                        }
                    }
                }*/
            }
        };

        //dragdrop for node dragging
        mContextDragDropped = new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {

                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                for (Node n : right_pane.getChildren()) {
                    target = (DraggableNode) n;

                    if (getId() != target.getId()) {
                        if (target.contem(self.getLayoutX(), self.getLayoutY(), self.getLayoutX() + self.getLargura(), self.getLayoutY() + self.getAltura())) {
                            //relocateToPoint(new Point2dSerial(target.getX() + mDragOffset.getX() + 107, target.getY() + target.getAltura() + mDragOffset.getY()));
                            for (Comando c : observer.getComandos()) {
                                for (Comando d : observer.getComandos()) {
                                    if ((c.getId() == target.getId() && d.getId() == self.getId())) {
                                        observer.getComandos().remove(d);
                                        observer.getComandos().add(observer.getComandos().indexOf(c) + 1, d);
                                        observer.update();
                                    }
                                }
                            }
                        }
                    }
                }

                Bounds objB = self.localToScene(self.getBoundsInLocal());
/*
                for (Node n : right_pane.getChildren()) {
                    target = (DraggableNode) n;
                    Bounds no;
                    no = n.localToScene(n.getBoundsInLocal());
                    if (objB.intersects(no) && getId() != n.getId()) {
                        int a = observer.getIndexComando(self);
                        int b = observer.getIndexComando(target);
                        Comando c = observer.getComando(self);
                        Comando d = observer.getComando(target);
                        System.out.println(a + " " + b);
                        observer.getComandos().remove(c);
                        observer.getComandos().add(b+1, c);
                        observer.update();
                    }
                }

*/                     
                

/*
                for (Node n : right_pane.getChildren()) {
                    //target = (DraggableNode) n;
                    Bounds target;
                    target = n.localToScene(n.getBoundsInLocal());

                    if (getId() != n.getId()) {
                        if (objB.intersects(target)) {
                            for (Comando c : observer.getComandos()) {
                                for (Comando d : observer.getComandos()) {
                                    if ((c.getId() == n.getId() && d.getId() == self.getId())) {
                                        observer.getComandos().add(observer.getComandos().indexOf(c) + 1, d);
                                        observer.getComandos().remove(d);
                                        observer.update();
                                    }
                                }
                            }
                        }
                    }
                }

*/                

                if (objB.intersects(objA)) {

                    AnchorPane parent = (AnchorPane) self.getParent();
                    parent.getChildren().remove(self);

                    for (Comando c : observer.getComandos()) {
                        if (c.getId() == self.getId()) {
                            observer.getComandos().remove(c);
                            observer.update();
                        }
                    }

                    observer.update();

                    //iterate each link id connected to this node
                    //find it's corresponding component in the right-hand
                    //AnchorPane and delete it.
                    //Note:  other nodes connected to these links are not being
                    //notified that the link has been removed.
                    for (ListIterator<String> iterId = mLinkIds.listIterator();
                            iterId.hasNext();) {

                        String id = iterId.next();

                        for (ListIterator<Node> iterNode = parent.getChildren().listIterator();
                                iterNode.hasNext();) {

                            Node node = iterNode.next();

                            if (node.getId() == null) {
                                continue;
                            }

                            if (node.getId().equals(id)) {
                                iterNode.remove();
                            }
                        }

                        iterId.remove();
                    }
                    //observer.setY_Cursor(observer.getY_Cursor()-50);
                    observer.setFlag(observer.getFlag() - 1);

                } else {

                    observer.update();

                }

                //System.out.println("X: " + self.getX() + ", Y: " + self.getY());
                //observer.carregar();
                event.setDropCompleted(true);
                event.consume();
            }

        };

        self.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                observer.carregar();
            }
        });

        /*
        //close button click
        close_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                AnchorPane parent = (AnchorPane) self.getParent();
                parent.getChildren().remove(self);

                //iterate each link id connected to this node
                //find it's corresponding component in the right-hand
                //AnchorPane and delete it.
                //Note:  other nodes connected to these links are not being
                //notified that the link has been removed.
                for (ListIterator<String> iterId = mLinkIds.listIterator();
                        iterId.hasNext();) {

                    String id = iterId.next();

                    for (ListIterator<Node> iterNode = parent.getChildren().listIterator();
                            iterNode.hasNext();) {

                        Node node = iterNode.next();

                        if (node.getId() == null) {
                            continue;
                        }

                        if (node.getId().equals(id)) {
                            iterNode.remove();
                        }
                    }

                    iterId.remove();
                }
            }

        });*/
//System.out.println("self: " + self.getY() + ", Y_Cursor: " + observer.getY_Cursor());
//if (self.getY() > (observer.getY_Cursor() - 50)) 
        //drag detection for node dragging
        self.setOnDragDetected(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("self: " + self.getY() + ", Y_Cursor: " + observer.getY_Cursor());

                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                getParent().setOnDragOver(mContextDragOver);
                getParent().setOnDragDropped(mContextDragDropped);
                /*
                    //begin drag ops
                    mDragOffset = new Point2D(event.getX(), event.getY());

                    relocateToPoint(
                            new Point2D(event.getSceneX(), event.getSceneY())
                    );
                 */
                ClipboardContent content = new ClipboardContent();
                DragContainer container = new DragContainer();

                container.addData("type", mType.toString());
                content.put(DragContainer.AddNode, container);

                //pass the UUID of the source node for later lookup
                container.addData("source", getId());

                startDragAndDrop(TransferMode.ANY).setContent(content);

            }

        });

    }

    public boolean contem(double x1, double y1, double x2, double y2) {

        if ((x1 > this.getX() && y1 > this.getY() && x1 < (this.getX() + this.getLargura()) && y1 < (this.getY() + this.getAltura()))
                || (x2 > this.getX() && y1 > this.getY() && x2 < (this.getX() + this.getLargura()) && y1 < (this.getY() + this.getAltura()))
                || (x1 > this.getX() && y2 > this.getY() && x1 < (this.getX() + this.getLargura()) && y2 < (this.getY() + this.getAltura()))
                || (x2 > this.getX() && y2 > this.getY() && x2 < (this.getX() + this.getLargura()) && y2 < (this.getY() + this.getAltura()))) //if ((x < this.getX() + this.getLargura()) && (x > this.getX() - this.getLargura()) && (y < this.getY() + this.getAltura()) && (y > this.getY() - this.getAltura())) {
        {
            return true;
        } else {
            return false;
        }
    }

}
