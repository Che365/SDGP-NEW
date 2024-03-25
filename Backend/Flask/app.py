from flask import Flask, request, jsonify
import os
import numpy as np
import tensorflow as tf
from tensorflow.keras.preprocessing.image import img_to_array, load_img

app = Flask(__name__)

# Load the saved model
loadmodel = tf.keras.models.load_model('trained_model.h5')
Categories = ['Caterpillar', 'Diabrotica speciosa', 'Healthy']

@app.route('/Predict', methods=['POST'])
def predict():
    # Get the image file from the request
    file = request.files['file']
    if not file:
        return jsonify({'error': 'No file uploaded'}), 400

    # Ensure the file is an image
    if not file.content_type.startswith('image'):
        return jsonify({'error': 'Invalid file type, only images are allowed'}), 400

    # Save the image to a temporary file
    temp_dir = app.config['UPLOAD_FOLDER']
    os.makedirs(temp_dir, exist_ok=True)
    image_path = os.path.join(temp_dir, 'temp.jpg')
    file.save(image_path)

    # Load and preprocess the image
    img = load_img(image_path, target_size=(50, 50))
    img_array = img_to_array(img)
    img_array = np.expand_dims(img_array, axis=0) / 255.0

    # Make predictions
    predictions = loadmode.predict(img_array)

    # Get the predicted class
    predicted_class_index = np.argmax(predictions)
    predicted_class = Categories[predicted_class_index]
    confidence = predictions[0][predicted_class_index]

    # Delete the temporary file
    os.remove(image_path)

    return jsonify({
        'predicted_class': predicted_class,
        'confidence': float(confidence),
        'category_mapping': Categories
    })

if __name__ == '__main__':
    app.config['UPLOAD_FOLDER'] = 'temp'
    app.run(debug=True)
