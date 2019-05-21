package com.zenika.academy.woodblocktoys.Block;

import com.zenika.academy.woodblocktoys.Color.Color;
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

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Data
@Slf4j
public class BlockFactory {
    private ColorRepository colorRepository;
    private ShapeRepository shapeRepository;
    private HeightRepository heightRepository;
    private WoodRepository woodRepository;
    private PaintRepository paintRepository;
    private BlockRepository blockRepository;

    public Block makeBlock() {
        log.info("Trying to generate a block");
        log.info("Fetching materials data");
        //import choices from database
        List<Color> colorChoices = (List<Color>) colorRepository.findAll();
        List<Shape> shapeChoices = (List<Shape>) shapeRepository.findAll();
        List<Height> heightChoices = (List<Height>) heightRepository.findAll();
        List<Wood> woodChoices = (List<Wood>) woodRepository.findAll();
        List<Paint> paintChoices = (List<Paint>) paintRepository.findAll();

        //base price
        double currentPrice = 0.35;

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
        Color colorPicked = colorChoices.get(0);
        Height heightPicked = heightChoices.get(0);
        Wood woodPicked = woodChoices.get(0);
        Paint paintPicked = paintChoices.get(0);
        Shape shapePicked = shapeChoices.get(0);

        //start compute price using block volume
        currentPrice += woodPicked.getVolumePrice() * 3 * randomVolume;

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

        //TODO Careful of conversions
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
