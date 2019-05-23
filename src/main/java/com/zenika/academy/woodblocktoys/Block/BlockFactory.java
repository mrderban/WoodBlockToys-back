package com.zenika.academy.woodblocktoys.Block;

import com.zenika.academy.woodblocktoys.Color.Color;
import com.zenika.academy.woodblocktoys.Color.ColorController;
import com.zenika.academy.woodblocktoys.Color.ColorRepository;
import com.zenika.academy.woodblocktoys.Finition.Finition;
import com.zenika.academy.woodblocktoys.Finition.FinitionRepository;
import com.zenika.academy.woodblocktoys.Height.Height;
import com.zenika.academy.woodblocktoys.Height.HeightRepository;
import com.zenika.academy.woodblocktoys.Shape.Shape;
import com.zenika.academy.woodblocktoys.Shape.ShapeRepository;
import com.zenika.academy.woodblocktoys.Wood.Wood;
import com.zenika.academy.woodblocktoys.Wood.WoodRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Data
@Slf4j
@Service
public class BlockFactory {

    /************************VARIABLES & CONSTRUCTOR************************/

    private final ColorRepository colorRepository;
    private final ShapeRepository shapeRepository;
    private final HeightRepository heightRepository;
    private final WoodRepository woodRepository;
    private final FinitionRepository finitionRepository;
    private final BlockRepository blockRepository;
    private final ColorController colorController;

    public BlockFactory(ColorRepository colorRepository, ShapeRepository shapeRepository, HeightRepository heightRepository, WoodRepository woodRepository, FinitionRepository finitionRepository, BlockRepository blockRepository, ColorController colorController) {
        this.colorRepository = colorRepository;
        this.shapeRepository = shapeRepository;
        this.heightRepository = heightRepository;
        this.woodRepository = woodRepository;
        this.finitionRepository = finitionRepository;
        this.blockRepository = blockRepository;
        this.colorController = colorController;
    }

    /************************HELPER METHODS************************/

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

    public List<Finition> getAllPaints() {
        List<Finition> finitions = new ArrayList<>();
        finitionRepository.findAll().forEach(finitions::add);
        return finitions;
    }

    public double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }

    public double roundToTwoDecimals(double d) {
        return Precision.round(d, 3);
    }

    /************************BLOCK FACTORY************************/

    public Block makeBlock(String paintChoice) {
        log.info("Trying to generate a block");
        log.info("Fetching materials data from database");
        List<Shape> shapeChoices = getAllShapes();
        List<Height> heightChoices = getAllHeights();
        List<Wood> woodChoices = getAllWoods();
        List<Color> colorChoices = getAllColors();

        //define base price
        double currentPrice = 0.35;

        log.info("Picking a random volume between 10 and 30 cm^3");
        int randomVolume = new Random().nextInt((30 - 10) + 1) + 10;

        log.info("Shuffling list of materials");
        try {
            Collections.shuffle(colorChoices);
            Collections.shuffle(shapeChoices);
            Collections.shuffle(heightChoices);
            Collections.shuffle(woodChoices);
        } catch (UnsupportedOperationException e) {
            log.info("The given list or its list-iterator does not support the set operation ", e);
        }

        //picking
        log.info("Picking materials from shuffled lists");
        Color colorPicked = colorChoices.get(0);
        Height heightPicked = heightChoices.get(0);
        Wood woodPicked = woodChoices.get(0);
        Finition finitionPicked = finitionRepository.findByType(paintChoice);
        Shape shapePicked = shapeChoices.get(0);

        //if no finition (raw type) reassign woodPicked to 'pin'
        if (finitionPicked.getType().equals("raw")) {
            woodPicked = woodRepository.findByType("pin");
        }

        log.info("Converting volume and surface prices from €/m² & €/m^3 to €/cm² & €/cm^3 ");
        woodPicked.setVolumePrice(woodPicked.getVolumePrice() * Math.pow(10, -6));
        finitionPicked.setSurfacePrice(finitionPicked.getSurfacePrice() * Math.pow(10, -4));

        log.info("Computing volume related price");
        currentPrice += woodPicked.getVolumePrice() * 3.0 * randomVolume;

        log.info("Computing block total area");
        double totalArea = 0;
        double sectionArea;
        double firstDim = 0;
        double secondDim = 0;

        switch (shapePicked.getType()) {
            case "circle":
                sectionArea = randomVolume / heightPicked.getValue();
                firstDim = Math.sqrt(sectionArea / Math.PI);
                totalArea = (2 * sectionArea) + (2 * Math.PI * firstDim * heightPicked.getValue());
                break;

            case "square":
                sectionArea = randomVolume / heightPicked.getValue();
                firstDim = Math.sqrt(sectionArea);
                totalArea = (2 * sectionArea) + (4 * firstDim * heightPicked.getValue());
                break;
        }

        //increment price using block total area
        currentPrice += (finitionPicked.getSurfacePrice() * 3.0 * totalArea);

        //add TVA
        currentPrice = currentPrice * 1.20;

        //build block and round double values to nearest 0.5
        Block currentBlock = Block.builder()
                .volume(randomVolume)
                .color(colorPicked)
                .wood(woodPicked)
                .finition(finitionPicked)
                .height(heightPicked)
                .shape(shapePicked)
                .price(roundToTwoDecimals(currentPrice))
                .area(roundToHalf(totalArea))
                .firstDim(roundToHalf(firstDim))
                .secondDim(roundToHalf(secondDim))
                .build();

        return blockRepository.save(currentBlock);
    }
}
