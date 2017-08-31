package application;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.ObservableList;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import static javafx.print.PrintColor.COLOR;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;

public class RootLayout extends AnchorPane {

    /**
     * @return the comandos
     */
    public ArrayList<Comando> getComandos() {
        return comandos;
    }

    public int getIndexComando(DraggableNode n) {
        for (Comando c : getComandos()) {
            if (c.getId() == n.getId()) {
                return comandos.indexOf(c);
            }
        }
        return 0;
    }

    public Comando getComando(DraggableNode n) {
        for (Comando c : getComandos()) {
            if (c.getId() == n.getId()) {
                return c;
            }
        }
        return null;
    }

    /**
     * @param comandos the comandos to set
     */
    public void setComandos(ArrayList<Comando> comandos) {
        this.comandos = comandos;
    }

    /**
     * @return the flag
     */
    public int getFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * @return the X_Cursor
     */
    public int getX_Cursor() {
        return X_Cursor;
    }

    /**
     * @param X_Cursor the X_Cursor to set
     */
    public void setX_Cursor(int X_Cursor) {
        this.X_Cursor = X_Cursor;
    }

    /**
     * @return the Y_Cursor
     */
    public int getY_Cursor() {
        return Y_Cursor;
    }

    /**
     * @param Y_Cursor the Y_Cursor to set
     */
    public void setY_Cursor(int Y_Cursor) {
        this.Y_Cursor = Y_Cursor;
    }

    @FXML
    SplitPane base_pane;
    @FXML
    AnchorPane right_pane;
    @FXML
    AnchorPane structure_pane;
    @FXML
    VBox left_pane;

    @FXML
    TextField id;
    @FXML
    TextField num;
    @FXML
    TextField tipo;
    @FXML
    TextField nome;
    @FXML
    TextField valor;
    @FXML
    Button atualizar;
    @FXML
    ImageView lixeira;

    ArrayList<Rectangle> estruturas;

    private ArrayList<Comando> comandos;

    private ArrayList<Comando> commandList;
    private ArrayList<Comando> elseList;

    int if_flag = 1, else_flag = 1;

    private EventHandler<DragEvent> mTrashDragOver;
    private EventHandler<DragEvent> mTrashDragDropped;

    private int flag = 0;
    int X_Cursor = 182;
    public int Y_Cursor = 75;

    public int Width = 25, Height = 25;

    boolean collision;

    int position;

    private DragIcon mDragOverIcon = null;

    private EventHandler<DragEvent> mDragTrash = null;

    private EventHandler<DragEvent> mIconDragOverRoot = null;
    private EventHandler<DragEvent> mIconDragDropped = null;
    private EventHandler<DragEvent> mIconDragOverRightPane = null;

    public RootLayout() {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/RootLayout.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    @FXML
    private void initialize() {

        //Add one icon that will be used for the drag-drop process
        //This is added as a child to the root anchorpane so it can be visible
        //on both sides of the split pane.
        mDragOverIcon = new DragIcon();

        mDragOverIcon.setVisible(false);
        mDragOverIcon.setOpacity(0.65);
        getChildren().add(mDragOverIcon);

        //populate left pane with multiple colored icons for testing
        for (int i = 0; i < 7; i++) {

            DragIcon icn = new DragIcon();

            addDragDetection(icn);

            icn.setType(DragIconType.values()[i]);
            left_pane.getChildren().add(icn);
        }

        setComandos(new ArrayList<Comando>());

        estruturas = new ArrayList<Rectangle>();

        Estrutura cabecalho = new Estrutura(structure_pane, estruturas, Color.NAVY, 50, 50, 500, 25, "cabecalho");
        Estrutura coluna = new Estrutura(structure_pane, estruturas, Color.NAVY, 50, 50, 25, 150, "coluna");
        Estrutura rodape = new Estrutura(structure_pane, estruturas, Color.NAVY, 50, 200, 500, 25, "rodape");

        right_pane.setBackground(Background.EMPTY);
        structure_pane.setBackground(Background.EMPTY);

        lixeira = new ImageView();
        lixeira.setId("lixeira");
        lixeira.setX(550);
        lixeira.setY(50);
        lixeira.setScaleX(80);
        lixeira.setScaleY(80);

        structure_pane.getChildren().add(lixeira);

        buildDragHandlers();
    }

    /*
    public void constroiCabecalho(Color color, int X, int Y, int Width, int Height, String Id) {
        Rectangle cabecalho = new Rectangle(X, Y, Width, Height);
        cabecalho.setId(Id);
        cabecalho.setX(X);
        cabecalho.setY(Y);
        cabecalho.setFill(color);
        estrutura.add(cabecalho);
        structure_pane.getChildren().add(cabecalho);
    }

    public void constroiColuna(Color color, int X, int Y, int Width, int Height, String Id) {
        Rectangle coluna = new Rectangle(X, Y, Width, Height);
        coluna.setId(Id);
        coluna.setX(X);
        coluna.setY(Y);
        coluna.setFill(color);
        estrutura.add(coluna);
        structure_pane.getChildren().add(coluna);
    }

    public void constroiRodape(Color color, int X, int Y, int Width, int Height, String Id) {
        Rectangle rodape = new Rectangle(X, Y, Width, Height);
        rodape.setId(Id);
        rodape.setX(X);
        rodape.setY(Y);
        rodape.setFill(color);
        estrutura.add(rodape);
        structure_pane.getChildren().add(rodape);
    }
     */
    private void addDragDetection(DragIcon dragIcon) {

        dragIcon.setOnDragDetected(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                // set drag event handlers on their respective objects
                base_pane.setOnDragOver(mIconDragOverRoot);
                right_pane.setOnDragOver(mIconDragOverRightPane);
                right_pane.setOnDragDropped(mIconDragDropped);

                // get a reference to the clicked DragIcon object
                DragIcon icn = (DragIcon) event.getSource();

                //begin drag ops
                mDragOverIcon.setType(icn.getType());
                mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                DragContainer container = new DragContainer();

                container.addData("type", mDragOverIcon.getType().toString());
                content.put(DragContainer.AddNode, container);

                mDragOverIcon.startDragAndDrop(TransferMode.ANY).setContent(content);
                mDragOverIcon.setVisible(true);
                mDragOverIcon.setMouseTransparent(true);
                event.consume();
            }
        });
    }

    /*
    public void checaInterseccaoEstruturas(ArrayList arrayList) {
        for (Rectangle m : estrutura) {
            for (Rectangle n : estrutura) {

                if (m.getId().equals("rodape")) {
                    if (n.getId().contains("rodape_") || n.getId().contains("coluna_")) {
                        Bounds A = m.localToScene(m.getBoundsInLocal());
                        Bounds B = n.localToScene(n.getBoundsInLocal());

                        if (A.intersects(B)) {
                            m.setY(m.getY() + 200);
                            for (Rectangle p : estrutura) {
                                if ((p.getId().equals("coluna"))) {
                                    p.setHeight(p.getHeight() + 200);
                                }
                            }
                        }

                        break;
                    }

                }
            }
        }
    }

    public void checaInterseccaoEntreEstruturas(ArrayList arrayList) {
        for (Rectangle m : estrutura) {
            for (Rectangle n : estrutura) {

                if (m.getId().contains("rodape_")) {
                    if (n.getId().contains("rodape_") || n.getId().contains("coluna_")) {
                        Bounds A = m.localToScene(m.getBoundsInLocal());
                        Bounds B = n.localToScene(n.getBoundsInLocal());

                        if (A.intersects(B)) {
                            m.setY(m.getY() + 200);
                            for (Rectangle p : estrutura) {
                                if ((p.getId().equals("coluna"))) {
                                    p.setHeight(p.getHeight() + 200);
                                }
                            }
                        }

                        break;
                    }

                }
            }
        }
    }
     */
    public void update() {
        right_pane.getChildren().clear();
        structure_pane.getChildren().removeAll(estruturas);
        estruturas.clear();

        Estrutura cabecalho = new Estrutura(structure_pane, estruturas, Color.NAVY, 50, 50, 500, 25, "cabecalho");
        Estrutura coluna = new Estrutura(structure_pane, estruturas, Color.NAVY, 50, 50, 25, (int) (getComandos().size() * 50 + 100), "coluna");
        Estrutura rodape = new Estrutura(structure_pane, estruturas, Color.NAVY, 50, (int) (getComandos().size() * 50 + 150), 500, 25, "rodape");

        for (Comando c : getComandos()) {
            c.draw(X_Cursor, Y_Cursor);
            Y_Cursor += 50;
        }

        for (Node n : right_pane.getChildren()) {
            n.setOpacity(1);
        }

        Y_Cursor = 75;
    }

    private void buildDragHandlers() {

        //drag over transition to move widget form left pane to right pane
        mIconDragOverRoot = new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {

                Point2D p = right_pane.sceneToLocal(event.getSceneX(), event.getSceneY());

                //turn on transfer mode and track in the right-pane's context 
                //if (and only if) the mouse cursor falls within the right pane's bounds.
                if (!right_pane.boundsInLocalProperty().get().contains(p)) {

                    event.acceptTransferModes(TransferMode.ANY);
                    mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                    return;
                }

                event.consume();
            }
        };

        mIconDragOverRightPane = new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {

                event.acceptTransferModes(TransferMode.ANY);

                //convert the mouse coordinates to scene coordinates,
                //then convert back to coordinates that are relative to 
                //the parent of mDragIcon.  Since mDragIcon is a child of the root
                //pane, coodinates must be in the root pane's coordinate system to work
                //properly.
                mDragOverIcon.relocateToPoint(
                        new Point2D(event.getSceneX(), event.getSceneY())
                );

                Bounds no = mDragOverIcon.localToScene(mDragOverIcon.getBoundsInLocal());
                position = 0;

                for (Node n : right_pane.getChildren()) {
                    //target = (DraggableNode) n;
                    Bounds target;
                    target = n.localToScene(n.getBoundsInLocal());
                    if (no.intersects(target)) {
                        n.setOpacity(0.3);
                        for (Comando d : getComandos()) {
                            if (d.getId() == n.getId()) {
                                position = comandos.indexOf(d);
                            }
                        }
                    } else {
                        n.setOpacity(1);
                        //position = comandos.size();
                    }
                }

                event.consume();
            }
        };

        mIconDragDropped = new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {

                DragContainer container
                        = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

                container.addData("scene_coords",
                        new Point2D(event.getSceneX(), event.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                content.put(DragContainer.AddNode, container);

                event.getDragboard().setContent(content);
                event.setDropCompleted(true);
            }
        };

        this.setOnDragDone(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {

                right_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
                right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
                base_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);

                mDragOverIcon.setVisible(false);

                //Create node drag operation
                DragContainer container
                        = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

                if (container != null) {
                    if (container.getValue("scene_coords") != null) {

                        DraggableNode node = new DraggableNode(RootLayout.this);
                        DraggableNode target = null;

                        node.setType(DragIconType.valueOf(container.getValue("type")));

                        /*
                        for (Node n : right_pane.getChildren()) {
                    target = (DraggableNode) n;
                    if (getId() != target.getId()) {
                        if (target.contem(node.getLayoutX(), node.getLayoutY(), node.getLayoutX() + node.getLargura(), node.getLayoutY() + node.getAltura())) {
                            System.out.println("COLISÃO DETECTADA ENTRE " + node.mType + " e " + target.mType);
                        } else {
                            System.out.println();
                        }
                    }
                }*/
                        switch (node.mType) {
                            case input:
                                ComandoInput input = new ComandoInput(right_pane, node);
                                if (position == 0) {
                                    getComandos().add(input);
                                } else {
                                    getComandos().add(position, input);
                                }
                                setFlag(getFlag() + 1);
                                break;
                            case var_const:
                                ComandoVar_Const var_const = new ComandoVar_Const(right_pane, node);
                                if (position == 0) {
                                    getComandos().add(var_const);
                                } else {
                                    getComandos().add(position, var_const);
                                }
                                setFlag(getFlag() + 1);
                                break;
                            case condition:
                                ComandoCondition condition = new ComandoCondition(right_pane, node);
                                if (position == 0) {
                                    getComandos().add(condition);
                                } else {
                                    getComandos().add(position, condition);
                                }
                                setFlag(getFlag() + 1);
                                break;
                            case elsee:
                                ComandoElse elsee = new ComandoElse(right_pane, node);
                                if (position == 0) {
                                    getComandos().add(elsee);
                                } else {
                                    getComandos().add(position, elsee);
                                }
                                setFlag(getFlag() + 1);
                                break;
                            case loop:
                                ComandoLoop loop = new ComandoLoop(right_pane, node);
                                if (position == 0) {
                                    getComandos().add(loop);
                                } else {
                                    getComandos().add(position, loop);
                                }
                                setFlag(getFlag() + 1);
                                break;
                            case arithmetic:
                                ComandoArithmetic arithmetic = new ComandoArithmetic(right_pane, node);
                                if (position == 0) {
                                    getComandos().add(arithmetic);
                                } else {
                                    getComandos().add(position, arithmetic);
                                }
                                setFlag(getFlag() + 1);
                                break;
                            case output:
                                ComandoOutput output = new ComandoOutput(right_pane, node);
                                if (position == 0) {
                                    getComandos().add(output);
                                } else {
                                    getComandos().add(position, output);
                                }
                                setFlag(getFlag() + 1);
                                break;
                            default:
                                break;
                        }

                        update();

                        /*
                        
                        right_pane.getChildren().add(node);
                        
                        if (getFlag() == 0) {
                            node.relocateToPoint(new Point2dSerial(182, 75));
                            setY_Cursor(getY_Cursor() + 50);
                            node.setX(node.getLayoutX());
                            node.setY(node.getLayoutY());
                            node.observer.carregar();
                            //event.setDropCompleted(true);
                            event.consume();
                            setFlag(getFlag() + 1);
                            System.out.println("flag: " + getFlag() + ", X_Cursor: " + getX_Cursor() + ", Y_Cursor: " + getY_Cursor());
                        } else {

                            Point2D cursorPoint = container.getValue("scene_coords");
                            
                            node.relocateToPoint(
                                    new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32)
                            );
                             
                            node.relocateToPoint(new Point2D(getX_Cursor() + 7, getY_Cursor()));
                            setY_Cursor(getY_Cursor() + 50);
                            node.setX(node.getLayoutX());
                            node.setY(node.getLayoutY());
                            node.observer.carregar();
                            //event.setDropCompleted(true);
                            event.consume();
                            setFlag(getFlag() + 1);
                            System.out.println("flag: " + getFlag() + ", X_Cursor: " + getX_Cursor() + ", Y_Cursor: " + getY_Cursor());
                        }

                        switch (node.mType) {

                            case loop:
                                setY_Cursor(getY_Cursor() - 50);
                                constroiCabecalho(Color.BLACK, 50 + Width, 50 + getY_Cursor(), 500 - Width, 25, "cabecalho_" + node.getId());
                                constroiColuna(Color.BLACK, 50 + Width, 50 + getY_Cursor(), 25, 200, "coluna_" + node.getId());
                                constroiRodape(Color.BLACK, 50 + Width, 250 + getY_Cursor(), 500 - Width, 25, "rodape_" + node.getId());

                                checaInterseccaoEstruturas(estrutura);
                                //checaInterseccaoEntreEstruturas(estrutura);
                                        
                                setY_Cursor(getY_Cursor() + 75);
                                setX_Cursor(getX_Cursor() + 25);
                                Width += 25;
                                break;

                            case condition:
                                setY_Cursor(getY_Cursor() - 50);
                                constroiCabecalho(Color.BLUE, 50 + Width, 50 + getY_Cursor(), 500 - Width, 25, "cabecalho_" + node.getId());
                                constroiColuna(Color.BLUE, 50 + Width, 50 + getY_Cursor(), 25, 200, "coluna_" + node.getId());
                                constroiRodape(Color.BLUE, 50 + Width, 250 + getY_Cursor(), 500 - Width, 25, "rodape_" + node.getId());
                                setY_Cursor(getY_Cursor() + 75);
                                setX_Cursor(getX_Cursor() + 25);
                                Width += 25;
                                break;

                            default:
                                break;
                        }

                        for (Rectangle n : estrutura) {
                            //System.out.println(n.getId() + ", " + n.getLayoutY());
                            System.out.println(n.getId() + ", " + n.getY());
                            if ((n.getId().equals("rodape")) && (getY_Cursor() > n.getY())) {
                                n.setY(getY_Cursor() + 150);
                                for (Rectangle p : estrutura) {
                                    if ((p.getId().equals("coluna"))) {
                                        p.setHeight(getY_Cursor() + 100);
                                    }
                                }
                            }
                        }
                         */
                    }
                }
            }
        }
        );
    }

}
