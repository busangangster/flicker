import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "../apis/axios";

interface AuthState {
  token: string | null;
  status: "idle" | "loading" | "succeeded" | "failed";
  error: string | null;
}

const initialState: AuthState = {
  token: null,
  status: "idle",
  error: null,
};


export const loginUser = createAsyncThunk(
  "auth/loginUser",
  async (
    credentials: { userId: string; password: string },
    { rejectWithValue }
  ) => {
    try {
      const response = await axios.post("/users/login", credentials);
      return response.data.token;
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    } catch (error) {
      return rejectWithValue("Login failed");
    }
  }
);

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    logout: (state) => {
      state.token = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginUser.pending, (state) => {
        state.status = "loading";
      })
      .addCase(loginUser.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.token = action.payload;
      })
      .addCase(loginUser.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload as string;
      });
  },
});

export const { logout } = authSlice.actions;

export default authSlice.reducer;
