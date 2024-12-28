// frontend/src/auth/AuthProvider.js
import React from "react";
import { AuthProvider as ContextAuthProvider } from "./AuthContext";

const AuthProvider = ({ children }) => {
  return <ContextAuthProvider>{children}</ContextAuthProvider>;
};
export default AuthProvider;
