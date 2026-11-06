package com.klinikpro.view.kit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.border.EmptyBorder;

/**
 * Wadah isi halaman. Lebarnya selalu mengikuti jendela, dan bisa digulir ke bawah
 * bila isinya lebih tinggi dari jendela.
 */
public class PanelHalaman extends JPanel implements Scrollable {

    public PanelHalaman() {
        super(new BorderLayout(0, 20));
        setBackground(Tema.LATAR);
        setBorder(new EmptyBorder(26, 30, 30, 30));
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }

    @Override
    public int getScrollableUnitIncrement(Rectangle r, int orientasi, int arah) { return 18; }

    @Override
    public int getScrollableBlockIncrement(Rectangle r, int orientasi, int arah) { return r.height - 40; }

    @Override
    public boolean getScrollableTracksViewportWidth() { return true; }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return getParent() != null && getParent().getHeight() > getPreferredSize().height;
    }
}
