// jest.config.js
import { createJestConfig } from 'vite-jest';
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

const viteConfig = defineConfig({
  plugins: [react()],
});

const jestConfig = createJestConfig({
  setupFilesAfterEnv: ['@testing-library/jest-dom/extend-expect'],
  testEnvironment: 'jest-environment-jsdom'
});

module.exports = jestConfig;
