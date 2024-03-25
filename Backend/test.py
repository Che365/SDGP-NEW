import os
import numpy as np
import tensorflow as tf
from tensorflow.keras.preprocessing.image import img_to_array, load_img
Categories = ['Caterpillar', 'Diabrotica speciosa', 'Healthy']

# Load the saved model
model = tf.keras.models.load_model('trained_model.h5')

# Load and preprocess the new image
url = 'E:\SDGP\Backend\bycbh73438-1\Caterpillar\caterpillar (3290).jpg'
img = load_img(url, target_size=(50, 50))
img_array = img_to_array(img)
img_array = np.expand_dims(img_array, axis=0) / 255.0

# Make predictions
predictions = model.predict(img_array)

# Get the predicted class
predicted_class_index = np.argmax(predictions)
predicted_class = Categories[predicted_class_index]
confidence = predictions[0][predicted_class_index]

print(f"The predicted class is: {predicted_class} with confidence: {confidence * 100}%")
