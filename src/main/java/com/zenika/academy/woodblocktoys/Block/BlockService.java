package com.zenika.academy.woodblocktoys.Block;

import com.zenika.academy.woodblocktoys.Color.Color;
import com.zenika.academy.woodblocktoys.Color.ColorController;
import com.zenika.academy.woodblocktoys.Color.ColorRepository;
import com.zenika.academy.woodblocktoys.Height.Height;
import com.zenika.academy.woodblocktoys.Height.HeightRepository;
import com.zenika.academy.woodblocktoys.Paint.Paint;
import com.zenika.academy.woodblocktoys.Paint.PaintRepository;
import com.zenika.academy.woodblocktoys.Shape.Shape;
import com.zenika.academy.woodblocktoys.Shape.ShapeRepository;
import com.zenika.academy.woodblocktoys.Wood.Wood;
import com.zenika.academy.woodblocktoys.Wood.WoodRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Data
@Slf4j
@Service
public class BlockService {
    private final ColorRepository colorRepository;
    private final ShapeRepository shapeRepository;
    private final HeightRepository heightRepository;
    private final WoodRepository woodRepository;
    private final PaintRepository paintRepository;
    private final BlockRepository blockRepository;
    private final ColorController colorController;

    public BlockService(ColorRepository colorRepository, ShapeRepository shapeRepository, HeightRepository heightRepository, WoodRepository woodRepository, PaintRepository paintRepository, BlockRepository blockRepository, ColorController colorController) {
        this.colorRepository = colorRepository;
        this.shapeRepository = shapeRepository;
        this.heightRepository = heightRepository;
        this.woodRepository = woodRepository;
        this.paintRepository = paintRepository;
        this.blockRepository = blockRepository;
        this.colorController = colorController;
    }

    public List<Color> getAllColors() {
        List<Color> colors = new ArrayList<>();
        colorRepository.findAll().forEach(colors::add);
        return colors;
    }

    public List<Height> getAllHeights() {
        List<Height> heights = new ArrayList<>();
        heightRepository.findAll().forEach(heights::add);
        return heights;
    }

    public List<Wood> getAllWoods() {
        List<Wood> woods = new ArrayList<>();
        woodRepository.findAll().forEach(woods::add);
        return woods;
    }

    public List<Shape> getAllShapes() {
        List<Shape> shapes = new ArrayList<>();
        shapeRepository.findAll().forEach(shapes::add);
        return shapes;
    }

    public List<Paint> getAllPaints() {
        List<Paint> paints = new ArrayList<>();
        paintRepository.findAll().forEach(paints::add);
        return paints;
    }

    public Block makeBlock() {
        log.info("Trying to generate a block");
        log.info("Fetching materials data");

        //import choices from database
        List<Shape> shapeChoices = getAllShapes();
        List<Height> heightChoices = getAllHeights();
        List<Wood> woodChoices = getAllWoods();
        List<Color> colorChoices = getAllColors();
        List<Paint> paintChoices = getAllPaints();

        //base price
        double currentPrice = 0.35;

        log.info("Picking a random volume");
        //pick random volume between 10 and 30 cm3
        int randomVolume = new Random().nextInt((30 - 10) + 1) + 10;

        //shuffle choices
        try {
            Collections.shuffle(colorChoices);
            Collections.shuffle(shapeChoices);
            Collections.shuffle(heightChoices);
            Collections.shuffle(woodChoices);
            Collections.shuffle(paintChoices);
        } catch (UnsupportedOperationException e) {
            log.info("The given list or its list-iterator does not support the set operation ", e);
        }

        //picking
        log.info("Picking materials");
        Color colorPicked = colorChoices.get(0);
        Height heightPicked = heightChoices.get(0);
        Wood woodPicked = woodChoices.get(0);
        Paint paintPicked = paintChoices.get(0);
        Shape shapePicked = shapeChoices.get(0);

        //start compute price using block volume
        currentPrice += woodPicked.getVolumePrice() * 3.0 * randomVolume;

        //compute area
        double currentArea = 0;
        double surfaceBase = 0;
        double firstDim = 0;
        double secondDim = 0;

        switch (shapePicked.getType()) {
            case "circle":
                surfaceBase = randomVolume / heightPicked.getValue();
                firstDim = Math.sqrt(surfaceBase / Math.PI);
                currentArea = (2 * surfaceBase) + (2 * Math.PI * firstDim * heightPicked.getValue());
                break;

            case "square":
                surfaceBase = randomVolume / heightPicked.getValue();
                firstDim = Math.sqrt(surfaceBase);
                currentArea = (2 * surfaceBase) + (4 * firstDim * heightPicked.getValue());
                break;
        }

        //increment price using block area
        currentPrice += (paintPicked.getSurfacePrice() * 3 * currentArea);

        //TODO Careful w/ conversions
        //add TVA
        currentPrice = currentPrice * 1.20;

        //build block
        Block currentBlock = Block.builder()
                .volume(randomVolume)
                .color(colorPicked)
                .wood(woodPicked)
                .paint(paintPicked)
                .height(heightPicked)
                .price(currentPrice)
                .area(currentArea)
                .firstDim(firstDim)
                .secondDim(secondDim)
                .build();

        return blockRepository.save(currentBlock);
    }
}
