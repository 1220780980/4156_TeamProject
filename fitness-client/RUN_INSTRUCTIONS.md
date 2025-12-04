# How to Run the Fitness Client Application

## Prerequisites

- Java 17+ installed
- Node.js and npm installed
- PostgreSQL database accessible (configured in application.properties)
- Maven installed (for backend)

## Step 1: Start the Backend Server

1. Navigate to the backend directory:

   ```bash
   cd fitness-client/backend
   ```

2. Start the Spring Boot application:

   ```bash
   mvn spring-boot:run
   ```

   Or if you prefer using an IDE, run the `FitnessApplication.java` main class.

3. Wait for the server to start. You should see:

   ```
   Started FitnessApplication in X.XXX seconds
   ```

   The backend will be running on **http://localhost:8081**

## Step 2: Start the Frontend Server

1. Open a **new terminal window** (keep the backend running)

2. Navigate to the frontend directory:

   ```bash
   cd fitness-client/frontend
   ```

3. Install dependencies (if not already installed):

   ```bash
   npm install
   ```

4. Start the React development server:

   ```bash
   npm start
   ```

5. The frontend will automatically open in your browser at **http://localhost:3000**

## Step 3: Verify the Changes

### Test User Registration/Login:

1. If you don't have an account, click "Sign Up" and create one
2. Fill in your details (email, password, age, height, weight, sex, activity level, fitness goal)
3. After registration, you'll be redirected to login
4. Login with your credentials

### Test Meal Plan Features:

1. After logging in, you should see a sidebar with "Profile" and "Meal Plans" options
2. Click on **"Meal Plans"** in the sidebar
3. You should see:
   - **Ingredient Search Section**: Try searching for a recipe by ingredient
   - **Meal Plan Form**: Fill in your preferences:
     - Meals per day (default: 3)
     - Maximum prep time (default: 60 minutes)
     - Add allergies (type and press Enter or click Add)
     - Add disliked meals
     - Add preferred ingredients
   - Click **"Generate Meal Plan"**

### Expected Behavior:

- The form should submit and show a message about the meal plan request
- You can click "Generate New Meal Plan" to create another one
- The like/dislike buttons on meals should work (visual feedback)
- The ingredient search will show an error message (backend endpoint not yet implemented)

## Troubleshooting

### Backend won't start:

- Check if port 8081 is already in use
- Verify database connection settings in `application.properties`
- Check Java version: `java -version` (should be 17+)

### Frontend won't start:

- Make sure Node.js is installed: `node -v`
- Try deleting `node_modules` and running `npm install` again
- Check if port 3000 is available

### CORS errors:

- Make sure backend is running on port 8081
- Check that `@CrossOrigin(origins = "http://localhost:3000")` is in the controller

### API connection errors:

- Verify backend is running: visit http://localhost:8081/api/users in browser (should show 405 or similar, not connection refused)
- Check browser console for detailed error messages

## Quick Test Commands

Test backend health:

```bash
curl http://localhost:8081/api/users
```

Test frontend API connection:

- Open browser DevTools (F12)
- Go to Network tab
- Try logging in or generating a meal plan
- Check for API calls to `localhost:8081`

## Notes

- The meal plan generation currently returns the request DTO (not actual meal plans from NutriFlow)
- Ingredient search endpoint needs backend implementation
- Full integration with NutriFlow API is pending backend development
