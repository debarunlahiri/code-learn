# Computer Vision

## Core tasks

- Image classification assigns one or more labels to an image.
- Object detection predicts classes and bounding boxes.
- Semantic segmentation labels every pixel by class.
- Instance segmentation separates individual objects of the same class.
- Keypoint detection locates landmarks such as joints.
- Optical character recognition converts image text into structured text.

The annotation method and metric must match the actual task. Image-level labels cannot directly supervise precise object boundaries without additional techniques.

## Image representation

An RGB image is usually a height × width × 3 tensor. Pixel values may be scaled to `[0, 1]` or normalized with channel statistics expected by a pretrained model. Incorrect colour order, resizing, or normalization can cause silent quality loss.

## Convolution

A convolutional filter slides across the image and shares weights across locations. Early layers often learn edges and textures; later layers combine them into task-specific shapes. Weight sharing makes CNNs more parameter-efficient than fully connected layers for images.

Stride controls movement of the filter. Padding controls border handling and output dimensions. Pooling reduces spatial resolution and adds some translation tolerance, although modern architectures may use strided convolutions instead.

## Transfer learning

A model pretrained on a large dataset supplies general visual representations. A common process is:

1. Replace the output head for the new labels.
2. Train the head while the backbone is frozen.
3. Unfreeze later layers and fine-tune with a smaller learning rate.
4. Compare against training more layers only if sufficient domain data exists.

Large domain shifts—such as natural images to medical scans—may require more careful pretraining, augmentation, and validation.

## Data augmentation

Augmentation encodes valid invariances. Horizontal flipping may be valid for general objects but invalid for text. Rotations may help aerial imagery but harm documents expected to be upright. Other techniques include crops, colour jitter, blur, noise, CutMix, and MixUp.

Apply augmentation only to training data and visually inspect transformed examples. An aggressive transformation can make labels incorrect.

## Object detection concepts

Intersection over Union measures overlap:

`IoU = intersection_area / union_area`

A predicted box is matched to a ground-truth box when class and IoU satisfy the evaluation rule. Non-maximum suppression removes highly overlapping duplicate predictions, keeping higher-confidence boxes. A low suppression threshold may remove nearby real objects; a high threshold may retain duplicates.

Mean average precision summarizes precision-recall behavior across classes and often multiple IoU thresholds. Always examine per-class results, object size, and crowded scenes.

## Segmentation metrics

Pixel accuracy can look strong when background dominates. IoU and Dice coefficient focus more directly on overlap. For rare small objects, report class-specific metrics and boundary quality if precise contours matter.

## Dataset quality

Look for duplicate or near-duplicate images, label inconsistency, camera-source imbalance, background shortcuts, and identity leakage. Split by source, location, patient, device, or capture session when those groups could otherwise appear on both sides.

Models often exploit shortcuts. A disease classifier may learn hospital markers rather than pathology. Use slice tests, occlusion studies, external validation, and domain review to detect such behavior.

## Serving

Preprocessing must exactly match training. Batch requests to use accelerators efficiently, but cap batching delay for latency-sensitive work. Consider resizing, quantization, pruning, compilation, and smaller architectures for edge devices. Monitor image resolution, brightness, blur, camera type, class distribution, latency, and confidence.

## Document AI pipeline

A robust document system may combine orientation correction, layout detection, OCR, table extraction, entity recognition, and validation rules. Preserve coordinates and confidence so a reviewer can trace each extracted value to the original page. Do not rely on language generation alone for high-impact numeric extraction without deterministic verification.

