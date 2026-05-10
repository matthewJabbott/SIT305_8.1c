import os
import json
from datetime import datetime
from flask import Flask, request, jsonify
from flask_cors import CORS
from google import genai

app = Flask(__name__)
CORS(app)

# Initialize Gemini Client
client = genai.Client(
    api_key="AIzaSyAHXNY-1AwXfER2IFF9td0T1rlLEWKpyP4",
    http_options={'api_version': 'v1'}
)

@app.route('/chat', methods=['POST'])
def chat():
    data = request.json
    user_message = data.get('message', '')
    username = data.get('username', 'User')

    prompt = (
        f"You are a helpful AI assistant for GlassBox AI. "
        f"The user '{username}' says: {user_message}. "
        f"Provide a concise, helpful response."
    )

    try:
        response = client.models.generate_content(
            model='gemini-2.5-flash', # Updated to current stable flash
            contents=prompt
        )
        
        return jsonify({
            "response": response.text.strip(),
            "timestamp": datetime.now().strftime("%H:%M") # Requirement #5
        })
    except Exception as e:
        print(f"Chat Error: {e}")
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080, debug=True)