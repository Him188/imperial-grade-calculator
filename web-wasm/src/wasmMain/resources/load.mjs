import {instantiate} from './imperial-grade-calculator.uninstantiated.mjs';

await wasmSetup;
instantiate({skia: Module['asm']});